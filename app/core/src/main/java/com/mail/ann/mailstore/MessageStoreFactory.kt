package com.mail.ann.mailstore

import com.mail.ann.Account

interface MessageStoreFactory {
    fun create(account: Account): ListenableMessageStore
}
