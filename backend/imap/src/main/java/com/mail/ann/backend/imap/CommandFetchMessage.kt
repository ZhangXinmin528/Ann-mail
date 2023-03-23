package com.mail.ann.backend.imap

import com.mail.ann.mail.BodyFactory
import com.mail.ann.mail.Part
import com.mail.ann.mail.store.imap.ImapStore
import com.mail.ann.mail.store.imap.OpenMode

internal class CommandFetchMessage(private val imapStore: ImapStore) {

    fun fetchPart(folderServerId: String, messageServerId: String, part: Part, bodyFactory: BodyFactory) {
        val folder = imapStore.getFolder(folderServerId)
        try {
            folder.open(OpenMode.READ_WRITE)

            val message = folder.getMessage(messageServerId)
            folder.fetchPart(message, part, bodyFactory, -1)
        } finally {
            folder.close()
        }
    }
}
