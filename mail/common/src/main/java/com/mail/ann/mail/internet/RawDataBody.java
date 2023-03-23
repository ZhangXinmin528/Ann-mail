package com.mail.ann.mail.internet;


import com.mail.ann.mail.Body;


/**
 * See {@link MimeUtility#decodeBody(Body)}
 */
public interface RawDataBody extends Body {
    String getEncoding();
}
