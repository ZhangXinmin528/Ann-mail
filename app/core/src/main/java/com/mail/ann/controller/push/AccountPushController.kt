package com.mail.ann.controller.push

import com.mail.ann.Account
import com.mail.ann.Account.FolderMode
import com.mail.ann.Preferences
import com.mail.ann.backend.BackendManager
import com.mail.ann.backend.api.BackendPusher
import com.mail.ann.backend.api.BackendPusherCallback
import com.mail.ann.controller.MessagingController
import com.mail.ann.mailstore.FolderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

internal class AccountPushController(
    private val backendManager: BackendManager,
    private val messagingController: MessagingController,
    private val preferences: Preferences,
    private val folderRepository: FolderRepository,
    backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val account: Account
) {
    private val coroutineScope = CoroutineScope(backgroundDispatcher)

    @Volatile
    private var backendPusher: BackendPusher? = null

    private val backendPusherCallback = object : BackendPusherCallback {
        override fun onPushEvent(folderServerId: String) {
            syncFolders(folderServerId)
        }

        override fun onPushError(exception: Exception) {
            messagingController.handleException(account, exception)
        }

        override fun onPushNotSupported() {
            Timber.v("AccountPushController(%s) - Push not supported. Disabling Push for account.", account.uuid)
            disablePush()
        }
    }

    fun start() {
        Timber.v("AccountPushController(%s).start()", account.uuid)
        startBackendPusher()
        startListeningForPushFolders()
    }

    fun stop() {
        Timber.v("AccountPushController(%s).stop()", account.uuid)
        stopListeningForPushFolders()
        stopBackendPusher()
    }

    fun reconnect() {
        Timber.v("AccountPushController(%s).reconnect()", account.uuid)
        backendPusher?.reconnect()
    }

    private fun startBackendPusher() {
        val backend = backendManager.getBackend(account)
        backendPusher = backend.createPusher(backendPusherCallback).also { backendPusher ->
            backendPusher.start()
        }
    }

    private fun stopBackendPusher() {
        backendPusher?.stop()
        backendPusher = null
    }

    private fun startListeningForPushFolders() {
        coroutineScope.launch {
            folderRepository.getPushFoldersFlow(account).collect { remoteFolders ->
                val folderServerIds = remoteFolders.map { it.serverId }
                updatePushFolders(folderServerIds)
            }
        }
    }

    private fun stopListeningForPushFolders() {
        coroutineScope.cancel()
    }

    private fun updatePushFolders(folderServerIds: List<String>) {
        Timber.v("AccountPushController(%s).updatePushFolders(): %s", account.uuid, folderServerIds)

        backendPusher?.updateFolders(folderServerIds)
    }

    private fun syncFolders(folderServerId: String) {
        messagingController.synchronizeMailboxBlocking(account, folderServerId)
    }

    private fun disablePush() {
        account.folderPushMode = FolderMode.NONE
        preferences.saveAccount(account)
    }
}
