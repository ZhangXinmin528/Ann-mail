package com.mail.ann.backend.imap

import com.mail.ann.backend.api.BackendPusher
import com.mail.ann.backend.api.BackendPusherCallback
import com.mail.ann.mail.AuthenticationFailedException
import com.mail.ann.mail.MessagingException
import com.mail.ann.mail.power.PowerManager
import com.mail.ann.mail.store.imap.IdleRefreshManager
import com.mail.ann.mail.store.imap.IdleRefreshTimeoutProvider
import com.mail.ann.mail.store.imap.IdleRefreshTimer
import com.mail.ann.mail.store.imap.ImapStore
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

private const val IO_ERROR_TIMEOUT = 5 * 60 * 1000L
private const val UNEXPECTED_ERROR_TIMEOUT = 60 * 60 * 1000L

/**
 * Manages [ImapFolderPusher] instances that listen for changes to individual folders.
 */
internal class ImapBackendPusher(
    private val imapStore: ImapStore,
    private val powerManager: PowerManager,
    private val idleRefreshManager: IdleRefreshManager,
    private val pushConfigProvider: ImapPushConfigProvider,
    private val callback: BackendPusherCallback,
    private val accountName: String,
    backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BackendPusher, ImapPusherCallback {
    private val coroutineScope = CoroutineScope(backgroundDispatcher)
    private val lock = Any()
    private val pushFolders = mutableMapOf<String, ImapFolderPusher>()
    private var currentFolderServerIds: Collection<String> = emptySet()
    private val pushFolderSleeping = mutableMapOf<String, IdleRefreshTimer>()

    private val idleRefreshTimeoutProvider = object : IdleRefreshTimeoutProvider {
        override val idleRefreshTimeoutMs
            get() = currentIdleRefreshMs
    }

    @Volatile
    private var currentMaxPushFolders = 0

    @Volatile
    private var currentIdleRefreshMs = 15 * 60 * 1000L

    override fun start() {
        coroutineScope.launch {
            pushConfigProvider.maxPushFoldersFlow.collect { maxPushFolders ->
                currentMaxPushFolders = maxPushFolders
                updateFolders()
            }
        }

        coroutineScope.launch {
            pushConfigProvider.idleRefreshMinutesFlow.collect { idleRefreshMinutes ->
                currentIdleRefreshMs = idleRefreshMinutes * 60 * 1000L
                refreshFolderTimers()
            }
        }
    }

    private fun refreshFolderTimers() {
        synchronized(lock) {
            for (pushFolder in pushFolders.values) {
                pushFolder.refresh()
            }
        }
    }

    override fun updateFolders(folderServerIds: Collection<String>) {
        updateFolders(folderServerIds, currentMaxPushFolders)
    }

    private fun updateFolders() {
        val currentFolderServerIds = synchronized(lock) { currentFolderServerIds }
        updateFolders(currentFolderServerIds, currentMaxPushFolders)
    }

    private fun updateFolders(folderServerIds: Collection<String>, maxPushFolders: Int) {
        Timber.v("ImapBackendPusher.updateFolders(): %s", folderServerIds)

        val pushFolderServerIds = if (folderServerIds.size > maxPushFolders) {
            folderServerIds.take(maxPushFolders).also { pushFolderServerIds ->
                Timber.v("..limiting Push to %d folders: %s", maxPushFolders, pushFolderServerIds)
            }
        } else {
            folderServerIds
        }

        val stopFolderPushers: List<ImapFolderPusher>
        val startFolderPushers: List<ImapFolderPusher>
        synchronized(lock) {
            currentFolderServerIds = folderServerIds

            val oldRunningFolderServerIds = pushFolders.keys
            val oldFolderServerIds = oldRunningFolderServerIds + pushFolderSleeping.keys
            val removeFolderServerIds = oldFolderServerIds - pushFolderServerIds
            stopFolderPushers = removeFolderServerIds
                .asSequence()
                .onEach { folderServerId -> cancelRetryTimer(folderServerId) }
                .map { folderServerId -> pushFolders.remove(folderServerId) }
                .filterNotNull()
                .toList()

            val startFolderServerIds = pushFolderServerIds - oldRunningFolderServerIds
            startFolderPushers = startFolderServerIds
                .asSequence()
                .filterNot { folderServerId -> isWaitingForRetry(folderServerId) }
                .onEach { folderServerId -> pushFolderSleeping.remove(folderServerId) }
                .map { folderServerId ->
                    createImapFolderPusher(folderServerId).also { folderPusher ->
                        pushFolders[folderServerId] = folderPusher
                    }
                }
                .toList()
        }

        for (folderPusher in stopFolderPushers) {
            folderPusher.stop()
        }

        for (folderPusher in startFolderPushers) {
            folderPusher.start()
        }
    }

    override fun stop() {
        Timber.v("ImapBackendPusher.stop()")

        coroutineScope.cancel()

        synchronized(lock) {
            for (pushFolder in pushFolders.values) {
                pushFolder.stop()
            }
            pushFolders.clear()

            for (retryTimer in pushFolderSleeping.values) {
                retryTimer.cancel()
            }
            pushFolderSleeping.clear()

            currentFolderServerIds = emptySet()
        }
    }

    override fun reconnect() {
        Timber.v("ImapBackendPusher.reconnect()")

        synchronized(lock) {
            for (pushFolder in pushFolders.values) {
                pushFolder.stop()
            }
            pushFolders.clear()

            for (retryTimer in pushFolderSleeping.values) {
                retryTimer.cancel()
            }
            pushFolderSleeping.clear()
        }

        imapStore.closeAllConnections()

        updateFolders()
    }

    private fun createImapFolderPusher(folderServerId: String): ImapFolderPusher {
        return ImapFolderPusher(
            imapStore,
            powerManager,
            idleRefreshManager,
            this,
            accountName,
            folderServerId,
            idleRefreshTimeoutProvider
        )
    }

    override fun onPushEvent(folderServerId: String) {
        callback.onPushEvent(folderServerId)
        idleRefreshManager.resetTimers()
    }

    override fun onPushError(folderServerId: String, exception: Exception) {
        synchronized(lock) {
            pushFolders.remove(folderServerId)

            when (exception) {
                is AuthenticationFailedException -> {
                    Timber.v(exception, "Authentication failure when attempting to use IDLE")
                    // TODO: This could be happening because of too many connections to the host. Ideally we'd want to
                    //  detect this case and use a lower timeout.

                    startRetryTimer(folderServerId, UNEXPECTED_ERROR_TIMEOUT)
                }
                is IOException -> {
                    Timber.v(exception, "I/O error while trying to use IDLE")

                    startRetryTimer(folderServerId, IO_ERROR_TIMEOUT)
                }
                is MessagingException -> {
                    Timber.v(exception, "MessagingException")

                    if (exception.isPermanentFailure) {
                        startRetryTimer(folderServerId, UNEXPECTED_ERROR_TIMEOUT)
                    } else {
                        startRetryTimer(folderServerId, IO_ERROR_TIMEOUT)
                    }
                }
                else -> {
                    Timber.v(exception, "Unexpected error")
                    startRetryTimer(folderServerId, UNEXPECTED_ERROR_TIMEOUT)
                }
            }

            if (pushFolders.isEmpty()) {
                callback.onPushError(exception)
            }
        }
    }

    override fun onPushNotSupported() {
        callback.onPushNotSupported()
    }

    private fun startRetryTimer(folderServerId: String, timeout: Long) {
        Timber.v("ImapBackendPusher for folder %s sleeping for %d ms", folderServerId, timeout)
        pushFolderSleeping[folderServerId] = idleRefreshManager.startTimer(timeout, ::restartFolderPushers)
    }

    private fun cancelRetryTimer(folderServerId: String) {
        Timber.v("Canceling ImapBackendPusher retry timer for folder %s", folderServerId)
        pushFolderSleeping.remove(folderServerId)?.cancel()
    }

    private fun isWaitingForRetry(folderServerId: String): Boolean {
        return pushFolderSleeping[folderServerId]?.isWaiting == true
    }

    private fun restartFolderPushers() {
        Timber.v("Refreshing ImapBackendPusher (at least one retry timer has expired)")

        updateFolders()
    }
}
