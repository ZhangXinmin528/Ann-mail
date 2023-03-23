package com.mail.ann.message.html

import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

object HtmlHelper {
    @JvmStatic
    fun extractText(html: String): String {
        return Jsoup.clean(html, Safelist.none())
    }
}
