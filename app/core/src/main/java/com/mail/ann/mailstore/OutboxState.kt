package com.mail.ann.mailstore

data class OutboxState(
    val sendState: SendState,
    val numberOfSendAttempts: Int,
    val sendError: String?,
    val sendErrorTimestamp: Long
)
