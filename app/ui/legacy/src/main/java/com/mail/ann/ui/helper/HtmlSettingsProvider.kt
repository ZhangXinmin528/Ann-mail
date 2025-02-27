package com.mail.ann.ui.helper

import com.mail.ann.Ann
import com.mail.ann.message.html.HtmlSettings
import com.mail.ann.ui.base.Theme
import com.mail.ann.ui.base.ThemeManager

class HtmlSettingsProvider(private val themeManager: ThemeManager) {
    fun createForMessageView() = HtmlSettings(
        useDarkMode = themeManager.messageViewTheme == Theme.DARK,
        useFixedWidthFont = Ann.isUseMessageViewFixedWidthFont
    )

    fun createForMessageCompose() = HtmlSettings(
        useDarkMode = themeManager.messageComposeTheme == Theme.DARK,
        useFixedWidthFont = false
    )
}
