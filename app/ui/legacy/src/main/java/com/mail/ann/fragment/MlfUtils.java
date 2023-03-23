package com.mail.ann.fragment;


import java.util.List;

import android.database.Cursor;
import android.text.TextUtils;

import com.mail.ann.Account;
import com.mail.ann.DI;
import com.mail.ann.Preferences;
import com.mail.ann.controller.MessageReference;
import com.mail.ann.helper.Utility;
import com.mail.ann.mail.Address;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mailstore.LocalFolder;
import com.mail.ann.mailstore.LocalStore;
import com.mail.ann.mailstore.LocalStoreProvider;

import static com.mail.ann.fragment.MLFProjectionInfo.SENDER_LIST_COLUMN;


public class MlfUtils {

    static LocalFolder getOpenFolder(long folderId, Account account) throws MessagingException {
        LocalStore localStore = DI.get(LocalStoreProvider.class).getInstance(account);
        LocalFolder localFolder = localStore.getFolder(folderId);
        localFolder.open();
        return localFolder;
    }

    static void setLastSelectedFolder(Preferences preferences, List<MessageReference> messages, long folderId) {
        MessageReference firstMsg = messages.get(0);
        Account account = preferences.getAccount(firstMsg.getAccountUuid());
        account.setLastSelectedFolderId(folderId);
    }

    static String getSenderAddressFromCursor(Cursor cursor) {
        String fromList = cursor.getString(SENDER_LIST_COLUMN);
        Address[] fromAddrs = Address.unpack(fromList);
        return (fromAddrs.length > 0) ? fromAddrs[0].getAddress() : null;
    }

    static String buildSubject(String subjectFromCursor, String emptySubject, int threadCount) {
        if (TextUtils.isEmpty(subjectFromCursor)) {
            return emptySubject;
        } else if (threadCount > 1) {
            // If this is a thread, strip the RE/FW from the subject.  "Be like Outlook."
            return Utility.stripSubject(subjectFromCursor);
        }
        return subjectFromCursor;
    }
}
