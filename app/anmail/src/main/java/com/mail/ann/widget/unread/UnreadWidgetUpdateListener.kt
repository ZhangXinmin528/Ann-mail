package com.mail.ann.widget.unread

import com.mail.ann.Account
import com.mail.ann.controller.SimpleMessagingListener
import com.mail.ann.mail.Message
import timber.log.Timber

class UnreadWidgetUpdateListener(private val unreadWidgetUpdater: UnreadWidgetUpdater) : SimpleMessagingListener() {

    private fun updateUnreadWidget() {
        try {
            unreadWidgetUpdater.updateAll()
        } catch (e: Exception) {
            Timber.e(e, "Error while updating unread widget(s)")
        }
    }

    override fun synchronizeMailboxRemovedMessage(account: Account, folderServerId: String, messageServerId: String) {
        updateUnreadWidget()
    }

    override fun synchronizeMailboxNewMessage(account: Account, folderServerId: String, message: Message) {
        updateUnreadWidget()
    }

    override fun folderStatusChanged(account: Account, folderId: Long) {
        updateUnreadWidget()
    }
}
