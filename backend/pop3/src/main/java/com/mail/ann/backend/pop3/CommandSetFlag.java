package com.mail.ann.backend.pop3;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mail.ann.mail.Flag;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.store.pop3.Pop3Folder;
import com.mail.ann.mail.store.pop3.Pop3Message;
import com.mail.ann.mail.store.pop3.Pop3Store;
import org.jetbrains.annotations.NotNull;


class CommandSetFlag {
    private final Pop3Store pop3Store;


    CommandSetFlag(Pop3Store pop3Store) {
        this.pop3Store = pop3Store;
    }

    void setFlag(@NotNull String folderServerId, @NotNull List<String> messageServerIds, @NotNull Flag flag,
            boolean newState) throws MessagingException {

        Pop3Folder remoteFolder = pop3Store.getFolder(folderServerId);
        if (!remoteFolder.isFlagSupported(flag)) {
            return;
        }

        try {
            remoteFolder.open();
            List<Pop3Message> messages = new ArrayList<>();
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
