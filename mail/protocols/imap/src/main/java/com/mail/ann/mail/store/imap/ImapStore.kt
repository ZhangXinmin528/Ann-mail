package com.mail.ann.mail.store.imap

import com.mail.ann.mail.MessagingException
import com.mail.ann.mail.ServerSettings
import com.mail.ann.mail.oauth.OAuth2TokenProvider
import com.mail.ann.mail.ssl.TrustedSocketFactory

interface ImapStore {
    @Throws(MessagingException::class)
    fun checkSettings()

    fun getFolder(name: String): ImapFolder

    @Throws(MessagingException::class)
    fun getFolders(): List<FolderListItem>

    fun closeAllConnections()

    companion object {
        fun create(
            serverSettings: ServerSettings,
            config: ImapStoreConfig,
            trustedSocketFactory: TrustedSocketFactory,
            oauthTokenProvider: OAuth2TokenProvider?
        ): ImapStore {
            return RealImapStore(serverSettings, config, trustedSocketFactory, oauthTokenProvider)
        }
    }
}
