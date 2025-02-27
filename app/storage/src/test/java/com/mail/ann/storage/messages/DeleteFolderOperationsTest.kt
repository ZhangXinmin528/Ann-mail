package com.mail.ann.storage.messages

import com.mail.ann.mailstore.StorageManager
import com.mail.ann.storage.RobolectricTest
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock

private const val ACCOUNT_UUID = "00000000-0000-4000-0000-000000000000"

class DeleteFolderOperationsTest : RobolectricTest() {
    private val messagePartDirectory = createRandomTempDirectory()
    private val sqliteDatabase = createDatabase()
    private val storageManager = mock<StorageManager> {
        on { getAttachmentDirectory(eq(ACCOUNT_UUID), anyOrNull()) } doReturn messagePartDirectory
    }
    private val lockableDatabase = createLockableDatabaseMock(sqliteDatabase)
    private val attachmentFileManager = AttachmentFileManager(storageManager, ACCOUNT_UUID)
    private val deleteFolderOperations = DeleteFolderOperations(lockableDatabase, attachmentFileManager)

    @After
    fun tearDown() {
        messagePartDirectory.deleteRecursively()
    }

    @Test
    fun `delete folder should remove message part files`() {
        createFolderWithMessage("delete", "message1")
        val messagePartId = createFolderWithMessage("retain", "message2")

        deleteFolderOperations.deleteFolders(listOf("delete"))

        val folders = sqliteDatabase.readFolders()
        assertThat(folders).hasSize(1)
        assertThat(folders.first().serverId).isEqualTo("retain")

        val messages = sqliteDatabase.readMessages()
        assertThat(messages).hasSize(1)
        assertThat(messages.first().uid).isEqualTo("message2")

        val messagePartFiles = messagePartDirectory.listFiles()
        assertThat(messagePartFiles).hasLength(1)
        assertThat(messagePartFiles!!.first().name).isEqualTo(messagePartId.toString())
    }

    private fun createFolderWithMessage(folderServerId: String, messageServerId: String): Long {
        val folderId = sqliteDatabase.createFolder(serverId = folderServerId)
        val messagePartId = sqliteDatabase.createMessagePart(dataLocation = 2, directory = messagePartDirectory)
        sqliteDatabase.createMessage(folderId = folderId, uid = messageServerId, messagePartId = messagePartId)

        return messagePartId
    }
}
