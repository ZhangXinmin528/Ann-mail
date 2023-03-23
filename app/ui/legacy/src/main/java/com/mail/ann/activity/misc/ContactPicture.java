package com.mail.ann.activity.misc;


import com.mail.ann.DI;
import com.mail.ann.contacts.ContactPictureLoader;


public class ContactPicture {

    public static ContactPictureLoader getContactPictureLoader() {
        return DI.get(ContactPictureLoader.class);
    }
}
