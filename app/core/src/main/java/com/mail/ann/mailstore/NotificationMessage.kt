package com.mail.ann.mailstore

data class NotificationMessage(
    val message: LocalMessage,
    val notificationId: Int?,
    val timestamp: Long
)
