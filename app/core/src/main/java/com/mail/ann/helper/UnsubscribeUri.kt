package com.mail.ann.helper

import android.net.Uri

sealed interface UnsubscribeUri {
    val uri: Uri
}

data class MailtoUnsubscribeUri(override val uri: Uri) : UnsubscribeUri
data class HttpsUnsubscribeUri(override val uri: Uri) : UnsubscribeUri
