package com.mail.ann.backend.imap

interface ImapPusherCallback {
    fun onPushEvent(folderServerId: String)
    fun onPushError(folderServerId: String, exception: Exception)
    fun onPushNotSupported()
}
