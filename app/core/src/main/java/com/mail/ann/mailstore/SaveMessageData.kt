package com.mail.ann.mailstore

import com.mail.ann.mail.Message
import com.mail.ann.mail.MessageDownloadState
import com.mail.ann.message.extractors.PreviewResult

data class SaveMessageData(
    val message: Message,
    val subject: String?,
    val date: Long,
    val internalDate: Long,
    val downloadState: MessageDownloadState,
    val attachmentCount: Int,
    val previewResult: PreviewResult,
    val textForSearchIndex: String? = null,
    val encryptionType: String?
)
