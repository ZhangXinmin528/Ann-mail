package com.mail.ann.controller;


import java.util.Comparator;

import com.mail.ann.mail.Message;


public class UidReverseComparator implements Comparator<Message> {
    @Override
    public int compare(Message messageLeft, Message messageRight) {
        Long uidLeft = getUidForMessage(messageLeft);
        Long uidRight = getUidForMessage(messageRight);

        if (uidLeft == null && uidRight == null) {
            return 0;
        } else if (uidLeft == null) {
            return 1;
        } else if (uidRight == null) {
            return -1;
        }

        // reverse order
        return uidRight.compareTo(uidLeft);
    }

    private Long getUidForMessage(Message message) {
        try {
            return Long.parseLong(message.getUid());
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }
}
