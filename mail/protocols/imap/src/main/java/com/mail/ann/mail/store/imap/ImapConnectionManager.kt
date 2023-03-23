package com.mail.ann.mail.store.imap

import com.mail.ann.mail.MessagingException

internal interface ImapConnectionManager {
    @Throws(MessagingException::class)
    fun getConnection(): ImapConnection

    fun releaseConnection(connection: ImapConnection?)
}
