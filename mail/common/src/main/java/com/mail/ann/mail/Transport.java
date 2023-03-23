
package com.mail.ann.mail;

public abstract class Transport {
    public abstract void open() throws MessagingException;

    public abstract void sendMessage(Message message) throws MessagingException;

    public abstract void close();
}
