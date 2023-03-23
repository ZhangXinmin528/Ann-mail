package com.mail.ann.widget.list

import android.content.Context
import com.mail.ann.Account
import com.mail.ann.controller.SimpleMessagingListener
import com.mail.ann.core.BuildConfig
import com.mail.ann.mail.Message
import timber.log.Timber

class MessageListWidgetUpdateListener(private val context: Context) : SimpleMessagingListener() {

    private fun updateMailListWidget() {
        try {
            MessageListWidgetProvider.triggerMessageListWidgetUpdate(context)
        } catch (e: RuntimeException) {
            if (BuildConfig.DEBUG) {
                throw e
            } else {
                Timber.e(e, "Error while updating message list widget")
            }
        }
    }

    override fun synchronizeMailboxRemovedMessage(account: Account, folderServerId: String, messageServerId: String) {
        updateMailListWidget()
    }

    override fun synchronizeMailboxNewMessage(account: Account, folderServerId: String, message: Message) {
        updateMailListWidget()
    }

    override fun folderStatusChanged(account: Account, folderId: Long) {
        updateMailListWidget()
    }
}
