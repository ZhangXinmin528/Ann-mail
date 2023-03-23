package com.mail.ann.message.html

import app.k9mail.html.cleaner.HtmlProcessor

class HtmlProcessorFactory(
    private val displayHtmlFactory: DisplayHtmlFactory
) {
    fun create(settings: HtmlSettings): HtmlProcessor {
        val displayHtml = displayHtmlFactory.create(settings)
        return HtmlProcessor(displayHtml)
    }
}
