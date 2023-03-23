package com.mail.ann.message.extractors;


import androidx.annotation.NonNull;
import timber.log.Timber;

import com.mail.ann.mail.Part;
import com.mail.ann.mail.internet.MessageExtractor;
import com.mail.ann.mail.internet.MimeUtility;
import com.mail.ann.message.SimpleMessageFormat;
import com.mail.ann.message.html.HtmlConverter;


//TODO: Get rid of this class and use MessageViewInfoExtractor instead
public class BodyTextExtractor {
    /** Fetch the body text from a messagePart in the desired messagePart format. This method handles
     * conversions between formats (html to text and vice versa) if necessary.
     */
    @NonNull
    public static String getBodyTextFromMessage(Part messagePart, SimpleMessageFormat format) {
        Part part;
        if (format == SimpleMessageFormat.HTML) {
            // HTML takes precedence, then text.
            part = MimeUtility.findFirstPartByMimeType(messagePart, "text/html");
            if (part != null) {
                Timber.d("getBodyTextFromMessage: HTML requested, HTML found.");
                return getTextFromPartOrEmpty(part);
            }

            part = MimeUtility.findFirstPartByMimeType(messagePart, "text/plain");
            if (part != null) {
                Timber.d("getBodyTextFromMessage: HTML requested, text found.");
                String text = getTextFromPartOrEmpty(part);
                return HtmlConverter.textToHtml(text);
            }
        } else if (format == SimpleMessageFormat.TEXT) {
            // Text takes precedence, then html.
            part = MimeUtility.findFirstPartByMimeType(messagePart, "text/plain");
            if (part != null) {
                Timber.d("getBodyTextFromMessage: Text requested, text found.");
                return getTextFromPartOrEmpty(part);
            }

            part = MimeUtility.findFirstPartByMimeType(messagePart, "text/html");
            if (part != null) {
                Timber.d("getBodyTextFromMessage: Text requested, HTML found.");
                String text = getTextFromPartOrEmpty(part);
                return HtmlConverter.htmlToText(text);
            }
        }

        // If we had nothing interesting, return an empty string.
        return "";
    }

    @NonNull
    private static String getTextFromPartOrEmpty(Part part) {
        String text = MessageExtractor.getTextFromPart(part);
        return text != null ? text : "";
    }
}
