package com.mail.ann.autocrypt

interface AutocryptStringProvider {
    fun transferMessageSubject(): String
    fun transferMessageBody(): String
}
