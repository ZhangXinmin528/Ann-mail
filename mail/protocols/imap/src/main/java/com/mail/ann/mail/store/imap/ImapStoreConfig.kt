package com.mail.ann.mail.store.imap

interface ImapStoreConfig {
    val logLabel: String
    fun isSubscribedFoldersOnly(): Boolean
    fun useCompression(): Boolean
}
