package com.mail.ann.storage.messages

import com.mail.ann.Account
import com.mail.ann.mailstore.ListenableMessageStore
import com.mail.ann.mailstore.LocalStoreProvider
import com.mail.ann.mailstore.MessageStoreFactory
import com.mail.ann.mailstore.NotifierMessageStore
import com.mail.ann.mailstore.StorageManager
import com.mail.ann.message.extractors.BasicPartInfoExtractor

class K9MessageStoreFactory(
    private val localStoreProvider: LocalStoreProvider,
    private val storageManager: StorageManager,
    private val basicPartInfoExtractor: BasicPartInfoExtractor
) : MessageStoreFactory {
    override fun create(account: Account): ListenableMessageStore {
        val localStore = localStoreProvider.getInstance(account)
        val messageStore = K9MessageStore(localStore.database, storageManager, basicPartInfoExtractor, account.uuid)
        val notifierMessageStore = NotifierMessageStore(messageStore, localStore)
        return ListenableMessageStore(notifierMessageStore)
    }
}
