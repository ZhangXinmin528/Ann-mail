package com.mail.ann.mailstore

import com.mail.ann.crypto.EncryptionExtractor
import com.mail.ann.mail.Message
import com.mail.ann.mail.MessageDownloadState
import com.mail.ann.message.extractors.AttachmentCounter
import com.mail.ann.message.extractors.MessageFulltextCreator
import com.mail.ann.message.extractors.MessagePreviewCreator

class SaveMessageDataCreator(
    private val encryptionExtractor: EncryptionExtractor,
    private val messagePreviewCreator: MessagePreviewCreator,
    private val messageFulltextCreator: MessageFulltextCreator,
    private val attachmentCounter: AttachmentCounter
) {
    fun createSaveMessageData(
        message: Message,
        downloadState: MessageDownloadState,
        subject: String? = null
    ): SaveMessageData {
        val now = System.currentTimeMillis()
        val date = message.sentDate?.time ?: now
        val internalDate = message.internalDate?.time ?: now
        val displaySubject = subject ?: message.subject

        val encryptionResult = encryptionExtractor.extractEncryption(message)
        return if (encryptionResult != null) {
            SaveMessageData(
                message = message,
                subject = displaySubject,
                date = date,
                internalDate = internalDate,
                downloadState = downloadState,
                attachmentCount = encryptionResult.attachmentCount,
                previewResult = encryptionResult.previewResult,
                textForSearchIndex = encryptionResult.textForSearchIndex,
                encryptionType = encryptionResult.encryptionType
            )
        } else {
            SaveMessageData(
                message = message,
                subject = displaySubject,
                date = date,
                internalDate = internalDate,
                downloadState = downloadState,
                attachmentCount = attachmentCounter.getAttachmentCount(message),
                previewResult = messagePreviewCreator.createPreview(message),
                textForSearchIndex = messageFulltextCreator.createFulltext(message),
                encryptionType = null
            )
        }
    }
}
