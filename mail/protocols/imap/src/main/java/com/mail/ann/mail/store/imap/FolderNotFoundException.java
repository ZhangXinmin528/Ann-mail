package com.mail.ann.mail.store.imap;


import com.mail.ann.mail.MessagingException;


public class FolderNotFoundException extends MessagingException {
    private final String folderServerId;


    public FolderNotFoundException(String folderServerId) {
        super("Folder not found: " + folderServerId, true);
        this.folderServerId = folderServerId;
    }

    public String getFolderServerId() {
        return folderServerId;
    }
}
