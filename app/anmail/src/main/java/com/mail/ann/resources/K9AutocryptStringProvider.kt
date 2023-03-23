package com.mail.ann.resources

import android.content.Context
import com.mail.ann.R
import com.mail.ann.autocrypt.AutocryptStringProvider

class K9AutocryptStringProvider(private val context: Context) : AutocryptStringProvider {
    override fun transferMessageSubject(): String = context.getString(R.string.ac_transfer_msg_subject)
    override fun transferMessageBody(): String = context.getString(R.string.ac_transfer_msg_body)
}
