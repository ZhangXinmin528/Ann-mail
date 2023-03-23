package com.mail.ann.backends

import android.content.Context
import com.mail.ann.Account
import com.mail.ann.backend.BackendFactory
import com.mail.ann.backend.api.Backend
import com.mail.ann.backend.imap.ImapBackend
import com.mail.ann.backend.imap.ImapPushConfigProvider
import com.mail.ann.mail.AuthType
import com.mail.ann.mail.power.PowerManager
import com.mail.ann.mail.ssl.TrustedSocketFactory
import com.mail.ann.mail.store.imap.IdleRefreshManager
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.ImapStoreConfig
import com.mail.ann.mail.transport.smtp.SmtpTransport
import com.mail.ann.mailstore.K9BackendStorageFactory
import com.mail.ann.preferences.AccountManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ImapBackendFactory(
    private val accountManager: AccountManager,
    private val powerManager: PowerManager,
    private val idleRefreshManager: IdleRefreshManager,
    private val backendStorageFactory: K9BackendStorageFactory,
    private val trustedSocketFactory: TrustedSocketFactory,
    private val context: Context
) : BackendFactory {
    override fun createBackend(account: Account): Backend {
        val accountName = account.displayName
        val backendStorage = backendStorageFactory.createBackendStorage(account)
        val imapStore = createImapStore(account)
        val pushConfigProvider = createPushConfigProvider(account)
        val smtpTransport = createSmtpTransport(account)

        return ImapBackend(
            accountName,
            backendStorage,
            imapStore,
            powerManager,
            idleRefreshManager,
            pushConfigProvider,
            smtpTransport
        )
    }

    private fun createImapStore(account: Account): ImapStore {
        val oAuth2TokenProvider = if (account.incomingServerSettings.authenticationType == AuthType.XOAUTH2) {
            RealOAuth2TokenProvider(context, accountManager, account)
        } else {
            null
        }

        val config = createImapStoreConfig(account)
        return ImapStore.create(
            account.incomingServerSettings,
            config,
            trustedSocketFactory,
            oAuth2TokenProvider
        )
    }

    private fun createImapStoreConfig(account: Account): ImapStoreConfig {
        return object : ImapStoreConfig {
            override val logLabel
                get() = account.uuid

            override fun isSubscribedFoldersOnly() = account.isSubscribedFoldersOnly

            override fun useCompression() = account.useCompression
        }
    }

    private fun createSmtpTransport(account: Account): SmtpTransport {
        val serverSettings = account.outgoingServerSettings
        val oauth2TokenProvider = if (serverSettings.authenticationType == AuthType.XOAUTH2) {
            RealOAuth2TokenProvider(context, accountManager, account)
        } else {
            null
        }

        return SmtpTransport(serverSettings, trustedSocketFactory, oauth2TokenProvider)
    }

    private fun createPushConfigProvider(account: Account) = object : ImapPushConfigProvider {
        override val maxPushFoldersFlow: Flow<Int>
            get() = accountManager.getAccountFlow(account.uuid)
                .map { it.maxPushFolders }
                .distinctUntilChanged()

        override val idleRefreshMinutesFlow: Flow<Int>
            get() = accountManager.getAccountFlow(account.uuid)
                .map { it.idleRefreshMinutes }
                .distinctUntilChanged()
    }
}
