package com.mail.ann.backend.jmap

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JmapUploadResponse(
    val accountId: String,
    val blobId: String,
    val type: String,
    val size: Long
)
