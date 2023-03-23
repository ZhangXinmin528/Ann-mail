package com.mail.ann.backend.imap

import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandFindByMessageId(private val imapStore: ImapStore) {

    fun findByMessageId(folderServerId: String, messageId: String): String? {
        val folder = imapStore.getFolder(folderServerId)
        try {
            folder.open(OpenMode.READ_WRITE)
            return folder.getUidFromMessageId(messageId)
        } finally {
            folder.close()
        }
    }
}
