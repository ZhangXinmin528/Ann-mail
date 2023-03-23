package com.mail.ann.ui.messageview

import android.content.Context
import android.os.Handler
import android.os.Message
import com.mail.ann.helper.ClipboardManager
import com.mail.ann.ui.R

internal class LinkTextHandler(
    private val context: Context,
    private val clipboardManager: ClipboardManager
) : Handler() {

    override fun handleMessage(message: Message) {
        val bundle = message.data
        val linkText = bundle.getString("title") ?: return

        val label = context.getString(R.string.webview_contextmenu_link_text_clipboard_label)
        clipboardManager.setText(label, linkText)
    }
}
