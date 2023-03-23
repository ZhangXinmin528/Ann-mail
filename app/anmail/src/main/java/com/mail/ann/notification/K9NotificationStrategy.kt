package com.mail.ann.notification

import com.mail.ann.Account
import com.mail.ann.Ann
import com.mail.ann.helper.Contacts
import com.mail.ann.mail.Flag
import com.mail.ann.mail.K9MailLib
import com.mail.ann.mail.Message
import com.mail.ann.mailstore.LocalFolder
import com.mail.ann.mailstore.LocalFolder.isModeMismatch
import com.mail.ann.mailstore.LocalMessage
import timber.log.Timber

class K9NotificationStrategy(private val contacts: Contacts) : NotificationStrategy {

    override fun shouldNotifyForMessage(
        account: Account,
        localFolder: LocalFolder,
        message: LocalMessage,
        isOldMessage: Boolean
    ): Boolean {

        if (!Ann.isNotificationDuringQuietTimeEnabled && Ann.isQuietTime) {
            Timber.v("No notification: Quiet time is active")
            return false
        }

        if (!account.isNotifyNewMail) {
            Timber.v("No notification: Notifications are disabled")
            return false
        }

        val folder = message.folder
        if (folder != null) {
            when (folder.databaseId) {
                account.inboxFolderId -> {
                    // Don't skip notifications if the Inbox folder is also configured as another special folder
                }
                account.trashFolderId -> {
                    Timber.v("No notification: Message is in Trash folder")
                    return false
                }
                account.draftsFolderId -> {
                    Timber.v("No notification: Message is in Drafts folder")
                    return false
                }
                account.spamFolderId -> {
                    Timber.v("No notification: Message is in Spam folder")
                    return false
                }
                account.sentFolderId -> {
                    Timber.v("No notification: Message is in Sent folder")
                    return false
                }
            }
        }

        if (isModeMismatch(account.folderDisplayMode, localFolder.displayClass)) {
            Timber.v("No notification: Message is in folder not being displayed")
            return false
        }

        if (isModeMismatch(account.folderNotifyNewMailMode, localFolder.notifyClass)) {
            Timber.v("No notification: Notifications are disabled for this folder class")
            return false
        }

        if (isOldMessage) {
            Timber.v("No notification: Message is old")
            return false
        }

        if (message.isSet(Flag.SEEN)) {
            Timber.v("No notification: Message is marked as read")
            return false
        }

        if (account.isIgnoreChatMessages && message.isChatMessage) {
            Timber.v("No notification: Notifications for chat messages are disabled")
            return false
        }

        if (!account.isNotifySelfNewMail && account.isAnIdentity(message.from)) {
            Timber.v("No notification: Notifications for messages from yourself are disabled")
            return false
        }

        if (account.isNotifyContactsMailOnly && !contacts.isAnyInContacts(message.from)) {
            Timber.v("No notification: Message is not from a known contact")
            return false
        }

        return true
    }

    private val Message.isChatMessage: Boolean
        get() = getHeader(K9MailLib.CHAT_HEADER).isNotEmpty()
}
