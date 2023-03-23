package com.mail.ann.mail.transport.smtp

data class EnhancedStatusCode(
    val statusClass: StatusCodeClass,
    val subject: Int,
    val detail: Int
)
