package com.mail.ann.mail.ssl

import java.io.File

fun interface KeyStoreDirectoryProvider {
    fun getDirectory(): File
}
