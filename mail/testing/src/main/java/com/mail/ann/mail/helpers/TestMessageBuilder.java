package com.mail.ann.mail.helpers;


import com.mail.ann.mail.Message;


public class TestMessageBuilder {
    String[] from;
    String[] to;
    boolean hasAttachments;
    long messageSize;


    public TestMessageBuilder from(String... email) {
        from = email;
        return this;
    }

    public TestMessageBuilder to(String... email) {
        to = email;
        return this;
    }

    public TestMessageBuilder setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
        return this;
    }

    public TestMessageBuilder messageSize(long messageSize) {
        this.messageSize = messageSize;
        return this;
    }
    
    public Message build() {
        return new TestMessage(this);
    }
}
