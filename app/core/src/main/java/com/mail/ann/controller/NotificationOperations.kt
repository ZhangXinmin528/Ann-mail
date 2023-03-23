package com.mail.ann.controller

import com.mail.ann.Account
import com.mail.ann.Preferences
import com.mail.ann.mailstore.MessageStoreManager
import com.mail.ann.notification.NotificationController
import com.mail.ann.search.LocalSearch
import com.mail.ann.search.isNewMessages
import com.mail.ann.search.isSingleFolder
import com.mail.ann.search.isUnifiedInbox

internal class NotificationOperations(
    private val notificationController: NotificationController,
    private val preferences: Preferences,
    private val messageStoreManager: MessageStoreManager
) {
    fun clearNotifications(search: LocalSearch) {
        if (search.isUnifiedInbox) {
            clearUnifiedInboxNotifications()
        } else if (search.isNewMessages) {
            clearAllNotifications()
        } else if (search.isSingleFolder) {
            val account = search.firstAccount() ?: return
            val folderId = search.folderIds.first()
            clearNotifications(account, folderId)
        } else {
            // TODO: Remove notifications when updating the message list. That way we can easily remove only
            //  notifications for messages that are currently displayed in the list.
        }
    }

    private fun clearUnifiedInboxNotifications() {
        for (account in preferences.accounts) {
            val messageStore = messageStoreManager.getMessageStore(account)

            val folderIds = messageStore.getFolders(excludeLocalOnly = true) { folderDetails ->
                if (folderDetails.isIntegrate) folderDetails.id else null
            }.filterNotNull().toSet()

            if (folderIds.isNotEmpty()) {
                notificationController.clearNewMailNotifications(account) { messageReferences ->
                    messageReferences.filter { messageReference -> messageReference.folderId in folderIds }
                }
            }
        }
    }

    private fun clearAllNotifications() {
        for (account in preferences.accounts) {
            notificationController.clearNewMailNotifications(account, clearNewMessageState = false)
        }
    }

    private fun clearNotifications(account: Account, folderId: Long) {
        notificationController.clearNewMailNotifications(account) { messageReferences ->
            messageReferences.filter { messageReference -> messageReference.folderId == folderId }
        }
    }

    private fun LocalSearch.firstAccount(): Account? {
        return preferences.getAccount(accountUuids.first())
    }
}
