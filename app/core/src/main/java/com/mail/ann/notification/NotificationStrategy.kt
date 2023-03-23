package com.mail.ann.notification

import com.mail.ann.Account
import com.mail.ann.mailstore.LocalFolder
import com.mail.ann.mailstore.LocalMessage

interface NotificationStrategy {

    fun shouldNotifyForMessage(
        account: Account,
        localFolder: LocalFolder,
        message: LocalMessage,
        isOldMessage: Boolean
    ): Boolean
}
