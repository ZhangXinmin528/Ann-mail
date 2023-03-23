package com.mail.ann.backend.webdav

import com.mail.ann.mail.Message
import com.mail.ann.mail.store.webdav.WebDavStore

internal class CommandUploadMessage(private val webDavStore: WebDavStore) {

    fun uploadMessage(folderServerId: String, message: Message): String? {
        val folder = webDavStore.getFolder(folderServerId)
        try {
            folder.open()

            folder.appendMessages(listOf(message))

            return null
        } finally {
            folder.close()
        }
    }
}
