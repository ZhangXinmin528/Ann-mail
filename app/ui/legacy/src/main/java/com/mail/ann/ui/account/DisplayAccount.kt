package com.mail.ann.ui.account

import com.mail.ann.Account

data class DisplayAccount(
    val account: Account,
    val unreadMessageCount: Int,
    val starredMessageCount: Int
)
