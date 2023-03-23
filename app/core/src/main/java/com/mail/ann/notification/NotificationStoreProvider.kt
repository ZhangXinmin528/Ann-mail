package com.mail.ann.notification

import com.mail.ann.Account

interface NotificationStoreProvider {
    fun getNotificationStore(account: Account): NotificationStore
}
