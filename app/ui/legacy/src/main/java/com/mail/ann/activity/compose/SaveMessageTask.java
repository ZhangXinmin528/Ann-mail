package com.mail.ann.activity.compose;


import android.os.AsyncTask;
import android.os.Handler;

import com.mail.ann.Account;
import com.mail.ann.activity.MessageCompose;
import com.mail.ann.controller.MessagingController;
import com.mail.ann.mail.Message;

public class SaveMessageTask extends AsyncTask<Void, Void, Void> {
    private final MessagingController messagingController;
    private final Account account;
    private final Handler handler;
    private final Message message;
    private final Long existingDraftId;
    private final String plaintextSubject;

    public SaveMessageTask(MessagingController messagingController, Account account, Handler handler, Message message,
            Long existingDraftId, String plaintextSubject) {
        this.messagingController = messagingController;
        this.account = account;
        this.handler = handler;
        this.message = message;
        this.existingDraftId = existingDraftId;
        this.plaintextSubject = plaintextSubject;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Long messageId = messagingController.saveDraft(account, message, existingDraftId, plaintextSubject);

        android.os.Message msg = android.os.Message.obtain(handler, MessageCompose.MSG_SAVED_DRAFT, messageId);
        handler.sendMessage(msg);

        return null;
    }
}
