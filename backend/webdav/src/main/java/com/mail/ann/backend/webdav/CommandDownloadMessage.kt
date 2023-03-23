package com.mail.ann.backend.webdav

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.mail.FetchProfile.Item.BODY
import com.mail.ann.mail.FetchProfile.Item.FLAGS
import com.mail.ann.mail.MessageDownloadState
import com.mail.ann.mail.helper.fetchProfileOf
import com.mail.ann.mail.store.webdav.WebDavStore

internal class CommandDownloadMessage(val backendStorage: BackendStorage, private val webDavStore: WebDavStore) {

    fun downloadCompleteMessage(folderServerId: String, messageServerId: String) {
        val folder = webDavStore.getFolder(folderServerId)
        try {
            val message = folder.getMessage(messageServerId)

            folder.fetch(listOf(message), fetchProfileOf(FLAGS, BODY), null, 0)

            val backendFolder = backendStorage.getFolder(folderServerId)
            backendFolder.saveMessage(message, MessageDownloadState.FULL)
        } finally {
            folder.close()
        }
    }
}
