package com.mail.ann.message.extractors;


import java.util.ArrayList;
import java.util.List;

import com.mail.ann.mail.Message;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.Part;
import com.mail.ann.mail.internet.MessageExtractor;


public class AttachmentCounter {

    public static AttachmentCounter newInstance() {
        return new AttachmentCounter();
    }

    public int getAttachmentCount(Message message) throws MessagingException {
        List<Part> attachmentParts = new ArrayList<>();
        MessageExtractor.findViewablesAndAttachments(message, null, attachmentParts);

        return attachmentParts.size();
    }
}
