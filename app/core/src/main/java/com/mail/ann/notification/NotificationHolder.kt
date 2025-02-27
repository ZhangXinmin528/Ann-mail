package com.mail.ann.notification

internal data class NotificationHolder(
    val notificationId: Int,
    val timestamp: Long,
    val content: NotificationContent
)

internal data class InactiveNotificationHolder(
    val timestamp: Long,
    val content: NotificationContent
)
