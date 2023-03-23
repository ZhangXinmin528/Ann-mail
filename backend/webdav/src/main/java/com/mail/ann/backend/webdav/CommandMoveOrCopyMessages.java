package com.mail.ann.backend.webdav;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.store.webdav.WebDavFolder;
import com.mail.ann.mail.store.webdav.WebDavMessage;
import com.mail.ann.mail.store.webdav.WebDavStore;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;


class CommandMoveOrCopyMessages {
    private final WebDavStore webDavStore;


    CommandMoveOrCopyMessages(WebDavStore webDavStore) {
        this.webDavStore = webDavStore;
    }

    Map<String, String> moveMessages(@NotNull String sourceFolderServerId, @NotNull String targetFolderServerId,
            @NotNull List<String> messageServerIds) throws MessagingException {
        return moveOrCopyMessages(sourceFolderServerId, targetFolderServerId, messageServerIds, false);
    }

    Map<String, String> copyMessages(@NotNull String sourceFolderServerId, @NotNull String targetFolderServerId,
            @NotNull List<String> messageServerIds) throws MessagingException {
        return moveOrCopyMessages(sourceFolderServerId, targetFolderServerId, messageServerIds, true);
    }

    private Map<String, String> moveOrCopyMessages(String srcFolder, String destFolder, Collection<String> uids,
            boolean isCopy) throws MessagingException {
        WebDavFolder remoteSrcFolder = null;
        WebDavFolder remoteDestFolder = null;

        try {
            remoteSrcFolder = webDavStore.getFolder(srcFolder);

            List<WebDavMessage> messages = new ArrayList<>();

            for (String uid : uids) {
                messages.add(remoteSrcFolder.getMessage(uid));
            }

            if (messages.isEmpty()) {
                Timber.i("processingPendingMoveOrCopy: no remote messages to move, skipping");
                return null;
            }

            remoteSrcFolder.open();

            Timber.d("processingPendingMoveOrCopy: source folder = %s, %d messages, " +
                    "destination folder = %s, isCopy = %s", srcFolder, messages.size(), destFolder, isCopy);


            remoteDestFolder = webDavStore.getFolder(destFolder);

            if (isCopy) {
                return remoteSrcFolder.copyMessages(messages, remoteDestFolder);
            } else {
                return remoteSrcFolder.moveMessages(messages, remoteDestFolder);
            }
        } finally {
            closeFolder(remoteSrcFolder);
            closeFolder(remoteDestFolder);
        }
    }

    private static void closeFolder(WebDavFolder folder) {
        if (folder != null) {
            folder.close();
        }
    }
}
