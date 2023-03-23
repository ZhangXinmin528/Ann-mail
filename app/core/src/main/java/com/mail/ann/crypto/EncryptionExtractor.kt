package com.mail.ann.crypto

import android.content.ContentValues
import com.mail.ann.mail.Message
import com.mail.ann.message.extractors.PreviewResult

interface EncryptionExtractor {
    fun extractEncryption(message: Message): EncryptionResult?
}

data class EncryptionResult(
    val encryptionType: String,
    val attachmentCount: Int,
    val previewResult: PreviewResult = PreviewResult.encrypted(),
    val textForSearchIndex: String? = null,
    val extraContentValues: ContentValues? = null
)
