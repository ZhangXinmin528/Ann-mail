package com.mail.ann.backend.jmap

data class JmapConfig(
    val username: String,
    val password: String,
    val baseUrl: String?,
    val accountId: String
)
