package com.mail.ann.notification

import com.mail.ann.Account
import com.mail.ann.controller.MessageReference
import com.mail.ann.mailstore.LocalStoreProvider
import com.mail.ann.mailstore.MessageStoreManager

internal class NotificationRepository(
    private val notificationStoreProvider: NotificationStoreProvider,
    private val localStoreProvider: LocalStoreProvider,
    private val messageStoreManager: MessageStoreManager,
    private val notificationContentCreator: NotificationContentCreator
) {
    private val notificationDataStore = NotificationDataStore()

    @Synchronized
    fun restoreNotifications(account: Account): NotificationData? {
        val localStore = localStoreProvider.getInstance(account)

        val (activeNotificationMessages, inactiveNotificationMessages) = localStore.notificationMessages.partition {
            it.notificationId != null
        }

        if (activeNotificationMessages.isEmpty()) return null

        val activeNotifications = activeNotificationMessages.map { notificationMessage ->
            val content = notificationContentCreator.createFromMessage(account, notificationMessage.message)
            NotificationHolder(notificationMessage.notificationId!!, notificationMessage.timestamp, content)
        }

        val inactiveNotifications = inactiveNotificationMessages.map { notificationMessage ->
            val content = notificationContentCreator.createFromMessage(account, notificationMessage.message)
            InactiveNotificationHolder(notificationMessage.timestamp, content)
        }

        return notificationDataStore.initializeAccount(account, activeNotifications, inactiveNotifications)
    }

    @Synchronized
    fun addNotification(account: Account, content: NotificationContent, timestamp: Long): AddNotificationResult? {
        return notificationDataStore.addNotification(account, content, timestamp)?.also { result ->
            persistNotificationDataStoreChanges(
                account = account,
                operations = result.notificationStoreOperations,
                updateNewMessageState = true
            )
        }
    }

    @Synchronized
    fun removeNotifications(
        account: Account,
        clearNewMessageState: Boolean = true,
        selector: (List<MessageReference>) -> List<MessageReference>
    ): RemoveNotificationsResult? {
        return notificationDataStore.removeNotifications(account, selector)?.also { result ->
            persistNotificationDataStoreChanges(
                account = account,
                operations = result.notificationStoreOperations,
                updateNewMessageState = clearNewMessageState
            )
        }
    }

    @Synchronized
    fun clearNotifications(account: Account, clearNewMessageState: Boolean) {
        notificationDataStore.clearNotifications(account)
        clearNotificationStore(account)

        if (clearNewMessageState) {
            clearNewMessageState(account)
        }
    }

    private fun persistNotificationDataStoreChanges(
        account: Account,
        operations: List<NotificationStoreOperation>,
        updateNewMessageState: Boolean
    ) {
        val notificationStore = notificationStoreProvider.getNotificationStore(account)
        notificationStore.persistNotificationChanges(operations)

        if (updateNewMessageState) {
            setNewMessageState(account, operations)
        }
    }

    private fun setNewMessageState(account: Account, operations: List<NotificationStoreOperation>) {
        val messageStore = messageStoreManager.getMessageStore(account)

        for (operation in operations) {
            when (operation) {
                is NotificationStoreOperation.Add -> {
                    val messageReference = operation.messageReference
                    messageStore.setNewMessageState(
                        folderId = messageReference.folderId,
                        messageServerId = messageReference.uid,
                        newMessage = true
                    )
                }
                is NotificationStoreOperation.Remove -> {
                    val messageReference = operation.messageReference
                    messageStore.setNewMessageState(
                        folderId = messageReference.folderId,
                        messageServerId = messageReference.uid,
                        newMessage = false
                    )
                }
                else -> Unit
            }
        }
    }

    private fun clearNewMessageState(account: Account) {
        val messageStore = messageStoreManager.getMessageStore(account)
        messageStore.clearNewMessageState()
    }

    private fun clearNotificationStore(account: Account) {
        val notificationStore = notificationStoreProvider.getNotificationStore(account)
        notificationStore.clearNotifications()
    }
}
