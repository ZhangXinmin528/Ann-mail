package com.mail.ann.mail.store.imap

import com.mail.ann.mail.Flag

internal interface InternalImapStore {
    val logLabel: String

    fun getCombinedPrefix(): String

    fun getPermanentFlagsIndex(): MutableSet<Flag>
}
