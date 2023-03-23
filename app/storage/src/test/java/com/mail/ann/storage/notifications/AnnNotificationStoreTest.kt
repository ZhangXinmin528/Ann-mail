package com.mail.ann.storage.notifications

import com.mail.ann.controller.MessageReference
import com.mail.ann.notification.NotificationStoreOperation
import com.mail.ann.storage.RobolectricTest
import com.mail.ann.storage.messages.createDatabase
import com.mail.ann.storage.messages.createLockableDatabaseMock
import com.mail.ann.storage.messages.createMessage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val FOLDER_ID = 1L

class AnnNotificationStoreTest : RobolectricTest() {
    private val sqliteDatabase = createDatabase()
    private val lockableDatabase = createLockableDatabaseMock(sqliteDatabase)
    private val store = K9NotificationStore(lockableDatabase)
    private val messageIdOne = sqliteDatabase.createMessage(folderId = FOLDER_ID, uid = "uid-1")
    private val messageIdTwo = sqliteDatabase.createMessage(folderId = FOLDER_ID, uid = "uid-2")

    @Test
    fun `add notification`() {
        val operations = listOf(
            NotificationStoreOperation.Add(
                messageReference = createMessageReference("uid-1"),
                notificationId = 42,
                timestamp = 23L
            )
        )

        store.persistNotificationChanges(operations)

        val notifications = sqliteDatabase.readNotifications()
        assertThat(notifications).hasSize(1)
        val notification = notifications.first()
        assertThat(notification.messageId).isEqualTo(messageIdOne)
        assertThat(notification.notificationId).isEqualTo(42)
        assertThat(notification.timestamp).isEqualTo(23L)
    }

    @Test
    fun `replace notification when adding`() {
        sqliteDatabase.createNotification(messageId = messageIdOne, notificationId = 10, timestamp = 20L)
        val operations = listOf(
            NotificationStoreOperation.ChangeToInactive(messageReference = createMessageReference("uid-1")),
            NotificationStoreOperation.Add(
                messageReference = createMessageReference("uid-2"),
                notificationId = 10,
                timestamp = 30L
            )
        )

        store.persistNotificationChanges(operations)

        val notifications = sqliteDatabase.readNotifications()
        assertThat(notifications).hasSize(2)
        val originalNotification = notifications.first { it.messageId == messageIdOne }
        assertThat(originalNotification.notificationId).isNull()
        assertThat(originalNotification.timestamp).isEqualTo(20L)
        val newNotification = notifications.first { it.messageId == messageIdTwo }
        assertThat(newNotification.notificationId).isEqualTo(10)
        assertThat(newNotification.timestamp).isEqualTo(30L)
    }

    @Test
    fun `remove notification`() {
        sqliteDatabase.createNotification(messageId = messageIdOne, notificationId = 10, timestamp = 20L)
        val operations = listOf(
            NotificationStoreOperation.Remove(messageReference = createMessageReference("uid-1"))
        )

        store.persistNotificationChanges(operations)

        val notifications = sqliteDatabase.readNotifications()
        assertThat(notifications).isEmpty()
    }

    @Test
    fun `replace notification when removing`() {
        sqliteDatabase.createNotification(messageId = messageIdOne, notificationId = null, timestamp = 20L)
        sqliteDatabase.createNotification(messageId = messageIdTwo, notificationId = 23, timestamp = 21L)
        val operations = listOf(
            NotificationStoreOperation.Remove(messageReference = createMessageReference("uid-2")),
            NotificationStoreOperation.ChangeToActive(
                messageReference = createMessageReference("uid-1"),
                notificationId = 23
            )
        )

        store.persistNotificationChanges(operations)

        val notifications = sqliteDatabase.readNotifications()
        assertThat(notifications).hasSize(1)
        val notification = notifications.first()
        assertThat(notification.messageId).isEqualTo(messageIdOne)
        assertThat(notification.notificationId).isEqualTo(23)
        assertThat(notification.timestamp).isEqualTo(20L)
    }

    private fun createMessageReference(uid: String): MessageReference {
        return MessageReference(accountUuid = "00000000-0000-4000-0000-000000000000", FOLDER_ID, uid)
    }
}
