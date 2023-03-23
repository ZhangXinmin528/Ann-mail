package com.mail.ann.external;

import java.util.Date;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.mail.ann.Account;
import com.mail.ann.Ann;
import com.mail.ann.R;
import com.mail.ann.helper.Contacts;
import com.mail.ann.helper.MessageHelper;
import com.mail.ann.mail.Address;
import com.mail.ann.mail.Flag;
import com.mail.ann.mail.Message.RecipientType;
import com.mail.ann.mailstore.LocalMessage;

class MessageInfoHolder {
    public Date compareDate;
    public CharSequence sender;
    public String senderAddress;
    public String uid;
    public boolean read;
    public LocalMessage message;
    public String uri;


    public static MessageInfoHolder create(Context context, LocalMessage message,
            Account account) {
        Contacts contactHelper = Ann.isShowContactName() ? Contacts.getInstance(context) : null;

        MessageInfoHolder target = new MessageInfoHolder();

        target.message = message;
        target.compareDate = message.getSentDate();
        if (target.compareDate == null) {
            target.compareDate = message.getInternalDate();
        }

        target.read = message.isSet(Flag.SEEN);

        Address[] addrs = message.getFrom();

        String counterParty;
        if (addrs.length > 0 &&  account.isAnIdentity(addrs[0])) {
            CharSequence to = MessageHelper.toFriendly(message.getRecipients(RecipientType.TO), contactHelper);
            counterParty = to.toString();
            target.sender = new SpannableStringBuilder(context.getString(R.string.message_to_label)).append(to);
        } else {
            target.sender = MessageHelper.toFriendly(addrs, contactHelper);
            counterParty = target.sender.toString();
        }

        if (addrs.length > 0) {
            target.senderAddress = addrs[0].getAddress();
        } else {
            // a reasonable fallback "whomever we were corresponding with
            target.senderAddress = counterParty;
        }

        target.uid = message.getUid();
        target.uri = message.getUri();

        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageInfoHolder)) {
            return false;
        }
        MessageInfoHolder other = (MessageInfoHolder)o;
        return message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
