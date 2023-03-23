package com.mail.ann.backend.imap

import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode
import timber.log.Timber

internal class CommandExpunge(private val imapStore: ImapStore) {

    fun expunge(folderServerId: String) {
        Timber.d("processPendingExpunge: folder = %s", folderServerId)

        val remoteFolder = imapStore.getFolder(folderServerId)
        try {
            if (!remoteFolder.exists()) return

            remoteFolder.open(OpenMode.READ_WRITE)
            if (remoteFolder.mode != OpenMode.READ_WRITE) return

            remoteFolder.expunge()

            Timber.d("processPendingExpunge: complete for folder = %s", folderServerId)
        } finally {
            remoteFolder.close()
        }
    }

    fun expungeMessages(folderServerId: String, messageServerIds: List<String>) {
        val remoteFolder = imapStore.getFolder(folderServerId)
        try {
            if (!remoteFolder.exists()) return

            remoteFolder.open(OpenMode.READ_WRITE)
            if (remoteFolder.mode != OpenMode.READ_WRITE) return

            remoteFolder.expungeUids(messageServerIds)
        } finally {
            remoteFolder.close()
        }
    }
}
