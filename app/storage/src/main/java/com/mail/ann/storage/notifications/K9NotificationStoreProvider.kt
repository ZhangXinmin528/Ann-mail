package com.mail.ann.storage.notifications

import com.mail.ann.Account
import com.mail.ann.mailstore.LocalStoreProvider
import com.mail.ann.notification.NotificationStore
import com.mail.ann.notification.NotificationStoreProvider

class K9NotificationStoreProvider(private val localStoreProvider: LocalStoreProvider) : NotificationStoreProvider {
    override fun getNotificationStore(account: Account): NotificationStore {
        val localStore = localStoreProvider.getInstance(account)
        return K9NotificationStore(lockableDatabase = localStore.database)
    }
}
