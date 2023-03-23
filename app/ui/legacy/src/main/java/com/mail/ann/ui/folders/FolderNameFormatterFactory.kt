package com.mail.ann.ui.folders

import android.content.Context

class FolderNameFormatterFactory {
    fun create(context: Context): FolderNameFormatter {
        return FolderNameFormatter(context.resources)
    }
}
