package com.mail.ann.backends

import com.mail.ann.Account
import com.mail.ann.backend.BackendFactory
import com.mail.ann.backend.api.Backend
import com.mail.ann.backend.pop3.Pop3Backend
import com.mail.ann.mail.oauth.OAuth2TokenProvider
import com.mail.ann.mail.ssl.TrustedSocketFactory
import com.mail.ann.mail.store.pop3.Pop3Store
import com.mail.ann.mail.transport.smtp.SmtpTransport
import com.mail.ann.mailstore.K9BackendStorageFactory

class Pop3BackendFactory(
    private val backendStorageFactory: K9BackendStorageFactory,
    private val trustedSocketFactory: TrustedSocketFactory
) : BackendFactory {
    override fun createBackend(account: Account): Backend {
        val accountName = account.displayName
        val backendStorage = backendStorageFactory.createBackendStorage(account)
        val pop3Store = createPop3Store(account)
        val smtpTransport = createSmtpTransport(account)
        return Pop3Backend(accountName, backendStorage, pop3Store, smtpTransport)
    }

    private fun createPop3Store(account: Account): Pop3Store {
        val serverSettings = account.incomingServerSettings
        return Pop3Store(serverSettings, trustedSocketFactory)
    }

    private fun createSmtpTransport(account: Account): SmtpTransport {
        val serverSettings = account.outgoingServerSettings
        val oauth2TokenProvider: OAuth2TokenProvider? = null
        return SmtpTransport(serverSettings, trustedSocketFactory, oauth2TokenProvider)
    }
}
