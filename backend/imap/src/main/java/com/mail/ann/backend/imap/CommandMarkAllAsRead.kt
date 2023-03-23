package com.mail.ann.backend.imap

import com.mail.ann.mail.Flag
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandMarkAllAsRead(private val imapStore: ImapStore) {

    fun markAllAsRead(folderServerId: String) {
        val remoteFolder = imapStore.getFolder(folderServerId)
        if (!remoteFolder.exists()) return

        try {
            remoteFolder.open(OpenMode.READ_WRITE)
            if (remoteFolder.mode != OpenMode.READ_WRITE) return

            remoteFolder.setFlags(setOf(Flag.SEEN), true)
        } finally {
            remoteFolder.close()
        }
    }
}
