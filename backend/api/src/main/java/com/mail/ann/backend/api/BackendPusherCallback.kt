package com.mail.ann.backend.api

interface BackendPusherCallback {
    fun onPushEvent(folderServerId: String)
    fun onPushError(exception: Exception)
    fun onPushNotSupported()
}
