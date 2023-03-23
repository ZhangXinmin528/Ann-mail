package com.mail.ann.backend.imap

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.mail.FetchProfile
import com.mail.ann.mail.FetchProfile.Item.BODY
import com.mail.ann.mail.FetchProfile.Item.ENVELOPE
import com.mail.ann.mail.FetchProfile.Item.FLAGS
import com.mail.ann.mail.FetchProfile.Item.STRUCTURE
import com.mail.ann.mail.MessageDownloadState
import com.mail.ann.mail.helper.fetchProfileOf
import com.mail.ann.mail.store.imap.ImapFolder
import com.mail.ann.mail.store.imap.ImapMessage
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandDownloadMessage(private val backendStorage: BackendStorage, private val imapStore: ImapStore) {

    fun downloadMessageStructure(folderServerId: String, messageServerId: String) {
        val folder = imapStore.getFolder(folderServerId)
        try {
            folder.open(OpenMode.READ_ONLY)

            val message = folder.getMessage(messageServerId)

            // fun fact: ImapFolder.fetch can't handle getting STRUCTURE at same time as headers
            fetchMessage(folder, message, fetchProfileOf(FLAGS, ENVELOPE))
            fetchMessage(folder, message, fetchProfileOf(STRUCTURE))

            val backendFolder = backendStorage.getFolder(folderServerId)
            backendFolder.saveMessage(message, MessageDownloadState.ENVELOPE)
        } finally {
            folder.close()
        }
    }

    fun downloadCompleteMessage(folderServerId: String, messageServerId: String) {
        val folder = imapStore.getFolder(folderServerId)
        try {
            folder.open(OpenMode.READ_ONLY)

            val message = folder.getMessage(messageServerId)
            fetchMessage(folder, message, fetchProfileOf(FLAGS, BODY))

            val backendFolder = backendStorage.getFolder(folderServerId)
            backendFolder.saveMessage(message, MessageDownloadState.FULL)
        } finally {
            folder.close()
        }
    }

    private fun fetchMessage(remoteFolder: ImapFolder, message: ImapMessage, fetchProfile: FetchProfile) {
        val maxDownloadSize = 0
        remoteFolder.fetch(listOf(message), fetchProfile, null, maxDownloadSize)
    }
}
