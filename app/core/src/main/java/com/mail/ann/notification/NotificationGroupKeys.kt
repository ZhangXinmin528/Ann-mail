package com.mail.ann.notification

import com.mail.ann.Account

object NotificationGroupKeys {
    private const val NOTIFICATION_GROUP_KEY_PREFIX = "newMailNotifications-"

    fun getGroupKey(account: Account): String {
        return NOTIFICATION_GROUP_KEY_PREFIX + account.accountNumber
    }
}
