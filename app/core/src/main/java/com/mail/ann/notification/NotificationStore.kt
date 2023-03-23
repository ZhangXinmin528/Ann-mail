package com.mail.ann.notification

interface NotificationStore {
    fun persistNotificationChanges(operations: List<NotificationStoreOperation>)
    fun clearNotifications()
}
