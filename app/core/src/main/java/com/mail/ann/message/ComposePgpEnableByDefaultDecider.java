package com.mail.ann.message;


import java.util.List;

import com.mail.ann.crypto.MessageCryptoStructureDetector;
import com.mail.ann.mail.Message;
import com.mail.ann.mail.Part;


public class ComposePgpEnableByDefaultDecider {
    public boolean shouldEncryptByDefault(Message localMessage) {
        return messageIsEncrypted(localMessage);
    }

    private boolean messageIsEncrypted(Message localMessage) {
        List<Part> encryptedParts = MessageCryptoStructureDetector.findMultipartEncryptedParts(localMessage);
        return !encryptedParts.isEmpty();
    }
}
