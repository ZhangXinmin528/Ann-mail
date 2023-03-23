package com.mail.ann.backend.pop3

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.mail.FetchProfile.Item.BODY
import com.mail.ann.mail.FetchProfile.Item.FLAGS
import com.mail.ann.mail.MessageDownloadState
import com.mail.ann.mail.helper.fetchProfileOf
import com.mail.ann.mail.store.pop3.Pop3Store

internal class CommandDownloadMessage(private val backendStorage: BackendStorage, private val pop3Store: Pop3Store) {

    fun downloadCompleteMessage(folderServerId: String, messageServerId: String) {
        val folder = pop3Store.getFolder(folderServerId)
        try {
            folder.open()

            val message = folder.getMessage(messageServerId)
            folder.fetch(listOf(message), fetchProfileOf(FLAGS, BODY), null, 0)

            val backendFolder = backendStorage.getFolder(folderServerId)
            backendFolder.saveMessage(message, MessageDownloadState.FULL)
        } finally {
            folder.close()
        }
    }
}
