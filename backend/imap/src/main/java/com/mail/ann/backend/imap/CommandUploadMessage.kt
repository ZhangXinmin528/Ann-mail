package com.mail.ann.backend.imap

import com.mail.ann.mail.Message
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandUploadMessage(private val imapStore: ImapStore) {

    fun uploadMessage(folderServerId: String, message: Message): String? {
        val folder = imapStore.getFolder(folderServerId)
        try {
            folder.open(OpenMode.READ_WRITE)

            val localUid = message.uid
            val uidMap = folder.appendMessages(listOf(message))

            return uidMap?.get(localUid)
        } finally {
            folder.close()
        }
    }
}
