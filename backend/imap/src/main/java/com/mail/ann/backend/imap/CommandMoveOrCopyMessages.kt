package com.mail.ann.backend.imap

import com.mail.ann.mail.MessagingException
import com.mail.ann.mail.store.imap.ImapFolder
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode
import timber.log.Timber

internal class CommandMoveOrCopyMessages(private val imapStore: ImapStore) {

    fun moveMessages(
        sourceFolderServerId: String,
        targetFolderServerId: String,
        messageServerIds: List<String>
    ): Map<String, String>? {
        return moveOrCopyMessages(sourceFolderServerId, targetFolderServerId, messageServerIds, false)
    }

    fun copyMessages(
        sourceFolderServerId: String,
        targetFolderServerId: String,
        messageServerIds: List<String>
    ): Map<String, String>? {
        return moveOrCopyMessages(sourceFolderServerId, targetFolderServerId, messageServerIds, true)
    }

    private fun moveOrCopyMessages(
        srcFolder: String,
        destFolder: String,
        uids: Collection<String>,
        isCopy: Boolean
    ): Map<String, String>? {
        var remoteSrcFolder: ImapFolder? = null
        var remoteDestFolder: ImapFolder? = null

        return try {
            remoteSrcFolder = imapStore.getFolder(srcFolder)

            if (uids.isEmpty()) {
                Timber.i("moveOrCopyMessages: no remote messages to move, skipping")
                return null
            }

            if (!remoteSrcFolder.exists()) {
                throw MessagingException("moveOrCopyMessages: remoteFolder $srcFolder does not exist", true)
            }

            remoteSrcFolder.open(OpenMode.READ_WRITE)
            if (remoteSrcFolder.mode != OpenMode.READ_WRITE) {
                throw MessagingException(
                    "moveOrCopyMessages: could not open remoteSrcFolder $srcFolder read/write", true
                )
            }

            val messages = uids.map { uid -> remoteSrcFolder.getMessage(uid) }

            Timber.d(
                "moveOrCopyMessages: source folder = %s, %d messages, destination folder = %s, isCopy = %s",
                srcFolder, messages.size, destFolder, isCopy
            )

            remoteDestFolder = imapStore.getFolder(destFolder)
            if (isCopy) {
                remoteSrcFolder.copyMessages(messages, remoteDestFolder)
            } else {
                remoteSrcFolder.moveMessages(messages, remoteDestFolder)
            }
        } finally {
            remoteSrcFolder?.close()
            remoteDestFolder?.close()
        }
    }
}
