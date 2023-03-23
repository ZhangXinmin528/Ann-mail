package com.mail.ann.mail.store.imap;


import java.io.IOException;

import com.mail.ann.mail.BodyFactory;
import com.mail.ann.mail.Part;
import com.mail.ann.mail.filter.FixedLengthInputStream;
import com.mail.ann.mail.internet.MimeHeader;


class FetchPartCallback implements ImapResponseCallback {
    private final Part part;
    private final BodyFactory bodyFactory;


    FetchPartCallback(Part part, BodyFactory bodyFactory) {
        this.part = part;
        this.bodyFactory = bodyFactory;
    }

    @Override
    public Object foundLiteral(ImapResponse response, FixedLengthInputStream literal) throws IOException {
        if (response.getTag() == null && ImapResponseParser.equalsIgnoreCase(response.get(1), "FETCH")) {
            //TODO: check for correct UID

            String contentTransferEncoding = part.getHeader(MimeHeader.HEADER_CONTENT_TRANSFER_ENCODING)[0];
            String contentType = part.getHeader(MimeHeader.HEADER_CONTENT_TYPE)[0];

            return bodyFactory.createBody(contentTransferEncoding, contentType, literal);
        }
        return null;
    }
}
