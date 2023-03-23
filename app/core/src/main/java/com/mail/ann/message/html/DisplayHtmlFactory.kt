package com.mail.ann.message.html

class DisplayHtmlFactory {
    fun create(settings: HtmlSettings): DisplayHtml {
        return DisplayHtml(settings)
    }
}
