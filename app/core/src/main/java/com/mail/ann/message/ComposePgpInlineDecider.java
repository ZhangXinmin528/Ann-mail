package com.mail.ann.message;


import java.util.List;

import com.mail.ann.crypto.MessageCryptoStructureDetector;
import com.mail.ann.mail.Message;
import com.mail.ann.mail.Part;


public class ComposePgpInlineDecider {
    public boolean shouldReplyInline(Message localMessage) {
        // TODO more criteria for this? maybe check the User-Agent header?
        return messageHasPgpInlineParts(localMessage);
    }

    private boolean messageHasPgpInlineParts(Message localMessage) {
        List<Part> inlineParts = MessageCryptoStructureDetector.findPgpInlineParts(localMessage);
        return !inlineParts.isEmpty();
    }
}
