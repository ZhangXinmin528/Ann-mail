package com.mail.ann.mailstore

import com.mail.ann.CoreResourceProvider
import com.mail.ann.message.extractors.AttachmentInfoExtractor
import com.mail.ann.message.html.HtmlProcessorFactory
import com.mail.ann.message.html.HtmlSettings

class MessageViewInfoExtractorFactory(
    private val attachmentInfoExtractor: AttachmentInfoExtractor,
    private val htmlProcessorFactory: HtmlProcessorFactory,
    private val resourceProvider: CoreResourceProvider
) {
    fun create(settings: HtmlSettings): MessageViewInfoExtractor {
        val htmlProcessor = htmlProcessorFactory.create(settings)
        return MessageViewInfoExtractor(attachmentInfoExtractor, htmlProcessor, resourceProvider)
    }
}
