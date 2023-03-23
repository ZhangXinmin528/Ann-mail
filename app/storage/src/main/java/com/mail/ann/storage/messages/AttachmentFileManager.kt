package com.mail.ann.storage.messages

import com.mail.ann.Ann
import com.mail.ann.helper.FileHelper
import com.mail.ann.mailstore.StorageManager
import com.mail.ann.mailstore.StorageManager.InternalStorageProvider
import java.io.File
import timber.log.Timber

internal class AttachmentFileManager(
    private val storageManager: StorageManager,
    private val accountUuid: String
) {
    fun deleteFile(messagePartId: Long) {
        val file = getAttachmentFile(messagePartId)
        if (file.exists() && !file.delete() && Ann.isDebugLoggingEnabled) {
            Timber.w("Couldn't delete message part file: %s", file.absolutePath)
        }
    }

    fun moveTemporaryFile(temporaryFile: File, messagePartId: Long) {
        val destinationFile = getAttachmentFile(messagePartId)
        FileHelper.renameOrMoveByCopying(temporaryFile, destinationFile)
    }

    fun copyFile(sourceMessagePartId: Long, destinationMessagePartId: Long) {
        val sourceFile = getAttachmentFile(sourceMessagePartId)
        val destinationFile = getAttachmentFile(destinationMessagePartId)
        sourceFile.copyTo(destinationFile)
    }

    fun getAttachmentFile(messagePartId: Long): File {
        val attachmentDirectory = storageManager.getAttachmentDirectory(accountUuid, InternalStorageProvider.ID)
        return File(attachmentDirectory, messagePartId.toString())
    }
}
