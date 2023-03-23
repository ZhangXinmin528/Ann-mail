
package com.mail.ann.mail;


public interface MessageRetrievalListener<T extends Message> {
    void messageFinished(T message);
}
