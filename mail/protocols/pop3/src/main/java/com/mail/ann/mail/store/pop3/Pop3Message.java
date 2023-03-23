package com.mail.ann.mail.store.pop3;


import com.mail.ann.mail.internet.MimeMessage;


public class Pop3Message extends MimeMessage {
    Pop3Message(String uid) {
        mUid = uid;
        mSize = -1;
    }

    public void setSize(int size) {
        mSize = size;
    }
}
