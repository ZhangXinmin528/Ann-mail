package com.mail.ann.ui.crypto;


import android.content.Intent;
import android.content.IntentSender;

import com.mail.ann.mailstore.MessageCryptoAnnotations;


public interface MessageCryptoCallback {
    void onCryptoHelperProgress(int current, int max);
    void onCryptoOperationsFinished(MessageCryptoAnnotations annotations);
    void startPendingIntentForCryptoHelper(IntentSender si, int requestCode, Intent fillIntent,
            int flagsMask, int flagValues, int extraFlags);
}
