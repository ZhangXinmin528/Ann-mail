package com.mail.ann.ui.helper

import com.mail.ann.message.html.DisplayHtml

class DisplayHtmlUiFactory(private val htmlSettingsProvider: HtmlSettingsProvider) {
    fun createForMessageView(): DisplayHtml {
        return DisplayHtml(htmlSettingsProvider.createForMessageView())
    }

    fun createForMessageCompose(): DisplayHtml {
        return DisplayHtml(htmlSettingsProvider.createForMessageCompose())
    }
}
