package com.mail.ann.backend.webdav;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mail.ann.mail.Flag;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.store.webdav.WebDavFolder;
import com.mail.ann.mail.store.webdav.WebDavMessage;
import com.mail.ann.mail.store.webdav.WebDavStore;
import org.jetbrains.annotations.NotNull;


class CommandSetFlag {
    private final WebDavStore webDavStore;


    CommandSetFlag(WebDavStore webDavStore) {
        this.webDavStore = webDavStore;
    }

    void setFlag(@NotNull String folderServerId, @NotNull List<String> messageServerIds, @NotNull Flag flag,
            boolean newState) throws MessagingException {

        WebDavFolder remoteFolder = webDavStore.getFolder(folderServerId);
        try {
            remoteFolder.open();

            List<WebDavMessage> messages = new ArrayList<>();
            for (String uid : messageServerIds) {
                messages.add(remoteFolder.getMessage(uid));
            }

            if (messages.isEmpty()) {
                return;
            }
            remoteFolder.setFlags(messages, Collections.singleton(flag), newState);
        } finally {
            remoteFolder.close();
        }
    }
}
