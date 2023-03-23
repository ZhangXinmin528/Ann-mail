package com.mail.ann.notification

import com.mail.ann.controller.MessageReference

internal data class NotificationContent(
    val messageReference: MessageReference,
    val sender: String,
    val subject: String,
    val preview: CharSequence,
    val summary: CharSequence
)
