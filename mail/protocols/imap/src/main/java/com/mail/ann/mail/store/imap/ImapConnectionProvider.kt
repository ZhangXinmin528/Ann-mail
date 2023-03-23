package com.mail.ann.mail.store.imap

internal interface ImapConnectionProvider {
    fun getConnection(folder: ImapFolder): ImapConnection?
}
