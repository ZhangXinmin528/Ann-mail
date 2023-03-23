package com.mail.ann.mail.store.webdav;

import com.mail.ann.logging.Timber;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.internet.MimeMessage;

import java.util.Locale;
import java.util.Map;

import static com.mail.ann.mail.helper.UrlEncodingHelper.decodeUtf8;
import static com.mail.ann.mail.helper.UrlEncodingHelper.encodeUtf8;

/**
 * A WebDav Message
 */
public class WebDavMessage extends MimeMessage {
    private final WebDavFolder mFolder;
    private String mUrl = "";


    WebDavMessage(String uid, WebDavFolder folder) {
        this.mUid = uid;
        this.mFolder = folder;
    }

    public void setUrl(String url) {
        // TODO: This is a not as ugly hack (ie, it will actually work). But it's still horrible
        // XXX: prevent URLs from getting to us that are broken
        if (!(url.toLowerCase(Locale.US).contains("http"))) {
            if (!(url.startsWith("/"))) {
                url = "/" + url;
            }
            url = ((WebDavFolder) mFolder).getUrl() + url;
        }

        String[] urlParts = url.split("/");
        int length = urlParts.length;
        String end = urlParts[length - 1];

        this.mUrl = "";
        url = "";

        /**
         * We have to decode, then encode the URL because Exchange likes to not properly encode all characters
         */
        try {
            end = decodeUtf8(end);
            end = encodeUtf8(end);
            end = end.replaceAll("\\+", "%20");
        } catch (IllegalArgumentException iae) {
            Timber.e(iae, "IllegalArgumentException caught in setUrl: ");
        }

        for (int i = 0; i < length - 1; i++) {
            if (i != 0) {
                url = url + "/" + urlParts[i];
            } else {
                url = urlParts[i];
            }
        }

        url = url + "/" + end;

        this.mUrl = url;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public void setNewHeaders(ParsedMessageEnvelope envelope) throws MessagingException {
        String[] headers = envelope.getHeaderList();
        Map<String, String> messageHeaders = envelope.getMessageHeaders();
        for (String header : headers) {
            String headerValue = messageHeaders.get(header);
            if (header.equals("Content-Length")) {
                int size = Integer.parseInt(headerValue);
                this.setSize(size);
            }

            if (headerValue != null &&
                    !headerValue.equals("")) {
                this.addHeader(header, headerValue);
            }
        }
    }
}
