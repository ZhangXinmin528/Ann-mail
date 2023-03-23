package com.mail.ann.backends

import com.mail.ann.Account
import com.mail.ann.backend.BackendFactory
import com.mail.ann.backend.api.Backend
import com.mail.ann.backend.webdav.WebDavBackend
import com.mail.ann.mail.ssl.TrustManagerFactory
import com.mail.ann.mail.store.webdav.DraftsFolderProvider
import com.mail.ann.mail.store.webdav.SniHostSetter
import com.mail.ann.mail.store.webdav.WebDavStore
import com.mail.ann.mail.transport.WebDavTransport
import com.mail.ann.mailstore.FolderRepository
import com.mail.ann.mailstore.K9BackendStorageFactory

class WebDavBackendFactory(
    private val backendStorageFactory: K9BackendStorageFactory,
    private val trustManagerFactory: TrustManagerFactory,
    private val sniHostSetter: SniHostSetter,
    private val folderRepository: FolderRepository
) : BackendFactory {
    override fun createBackend(account: Account): Backend {
        val accountName = account.displayName
        val backendStorage = backendStorageFactory.createBackendStorage(account)
        val serverSettings = account.incomingServerSettings
        val draftsFolderProvider = createDraftsFolderProvider(account)
        val webDavStore = WebDavStore(trustManagerFactory, sniHostSetter, serverSettings, draftsFolderProvider)
        val webDavTransport = WebDavTransport(trustManagerFactory, sniHostSetter, serverSettings, draftsFolderProvider)
        return WebDavBackend(accountName, backendStorage, webDavStore, webDavTransport)
    }

    private fun createDraftsFolderProvider(account: Account): DraftsFolderProvider {
        return DraftsFolderProvider {
            val draftsFolderId = account.draftsFolderId ?: error("No Drafts folder configured")
            folderRepository.getFolderServerId(account, draftsFolderId) ?: error("Couldn't find local Drafts folder")
        }
    }
}
