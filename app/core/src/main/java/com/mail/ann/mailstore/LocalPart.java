package com.mail.ann.mailstore;


public interface LocalPart {
    String getAccountUuid();
    long getPartId();
    long getSize();
    LocalMessage getMessage();
}
