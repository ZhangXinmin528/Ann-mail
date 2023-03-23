package com.mail.ann.notification

internal data class RemoveNotificationsResult(
    val notificationData: NotificationData,
    val notificationStoreOperations: List<NotificationStoreOperation>,
    val notificationHolders: List<NotificationHolder>,
    val cancelNotificationIds: List<Int>
)
