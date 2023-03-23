package com.mail.ann.backend.imap

import com.mail.ann.mail.Flag
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandSetFlag(private val imapStore: ImapStore) {

    fun setFlag(folderServerId: String, messageServerIds: List<String>, flag: Flag, newState: Boolean) {
        if (messageServerIds.isEmpty()) return

        val remoteFolder = imapStore.getFolder(folderServerId)
        if (!remoteFolder.exists()) return

        try {
            remoteFolder.open(OpenMode.READ_WRITE)
            if (remoteFolder.mode != OpenMode.READ_WRITE) return

            val messages = messageServerIds.map { uid -> remoteFolder.getMessage(uid) }

            remoteFolder.setFlags(messages, setOf(flag), newState)
        } finally {
            remoteFolder.close()
        }
    }
}
