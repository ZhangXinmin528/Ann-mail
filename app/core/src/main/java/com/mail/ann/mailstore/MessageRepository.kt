package com.mail.ann.mailstore

import com.mail.ann.controller.MessageReference
import com.mail.ann.mail.Header

class MessageRepository(private val messageStoreManager: MessageStoreManager) {
    fun getHeaders(messageReference: MessageReference): List<Header> {
        val messageStore = messageStoreManager.getMessageStore(messageReference.accountUuid)
        return messageStore.getHeaders(messageReference.folderId, messageReference.uid)
    }
}
