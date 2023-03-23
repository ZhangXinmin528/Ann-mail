package com.mail.ann.mail.store.imap;


import com.mail.ann.mail.internet.MimeMessage;


public class ImapMessage extends MimeMessage {
    ImapMessage(String uid) {
        this.mUid = uid;
    }

    public void setSize(int size) {
        this.mSize = size;
    }
}
