package com.mail.ann.helper

import android.content.Context
import com.mail.ann.mail.ssl.KeyStoreDirectoryProvider
import java.io.File

internal class AndroidKeyStoreDirectoryProvider(private val context: Context) : KeyStoreDirectoryProvider {
    override fun getDirectory(): File {
        return context.getDir("KeyStore", Context.MODE_PRIVATE)
    }
}
