package com.mail.ann.mail.transport.smtp

interface SmtpLogger {
    val isRawProtocolLoggingEnabled: Boolean

    fun log(message: String, vararg args: Any?) = log(throwable = null, message, *args)

    fun log(throwable: Throwable?, message: String, vararg args: Any?)
}
