package com.mail.ann.backend.imap

import com.mail.ann.mail.Flag
import com.mail.ann.mail.MessagingException
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandDeleteAll(private val imapStore: ImapStore) {

    @Throws(MessagingException::class)
    fun deleteAll(folderServerId: String) {
        val remoteFolder = imapStore.getFolder(folderServerId)
        if (!remoteFolder.exists()) {
            return
        }

        try {
            remoteFolder.open(OpenMode.READ_WRITE)
            remoteFolder.setFlags(setOf(Flag.DELETED), true)
        } finally {
            remoteFolder.close()
        }
    }
}
