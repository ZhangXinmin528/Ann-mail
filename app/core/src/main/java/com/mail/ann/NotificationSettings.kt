package com.mail.ann

/**
 * Describes how a notification should behave.
 */
data class NotificationSettings(
    val isRingEnabled: Boolean = false,
    val ringtone: String? = null,
    val light: NotificationLight = NotificationLight.Disabled,
    val vibration: NotificationVibration = NotificationVibration.DEFAULT
)
