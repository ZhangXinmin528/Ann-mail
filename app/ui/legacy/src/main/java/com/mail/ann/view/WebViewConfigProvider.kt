package com.mail.ann.view

import com.mail.ann.K9
import com.mail.ann.ui.base.Theme
import com.mail.ann.ui.base.ThemeManager

class WebViewConfigProvider(private val themeManager: ThemeManager) {
    fun createForMessageView() = createWebViewConfig(themeManager.messageViewTheme)

    fun createForMessageCompose() = createWebViewConfig(themeManager.messageComposeTheme)

    private fun createWebViewConfig(theme: Theme): WebViewConfig {
        return WebViewConfig(
            useDarkMode = theme == Theme.DARK,
            autoFitWidth = K9.isAutoFitWidth,
            textZoom = K9.fontSizes.messageViewContentAsPercent
        )
    }
}
