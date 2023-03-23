package com.mail.ann.mail.transport;


import java.util.Collections;

import com.mail.ann.logging.Timber;
import com.mail.ann.mail.K9MailLib;
import com.mail.ann.mail.Message;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.ServerSettings;
import com.mail.ann.mail.Transport;
import com.mail.ann.mail.ssl.TrustManagerFactory;
import com.mail.ann.mail.store.webdav.DraftsFolderProvider;
import com.mail.ann.mail.store.webdav.SniHostSetter;
import com.mail.ann.mail.store.webdav.WebDavStore;

public class WebDavTransport extends Transport {
    private WebDavStore store;

    public WebDavTransport(TrustManagerFactory trustManagerFactory, SniHostSetter sniHostSetter,
            ServerSettings serverSettings, DraftsFolderProvider draftsFolderProvider) {
        store = new WebDavStore(trustManagerFactory, sniHostSetter, serverSettings, draftsFolderProvider);

        if (K9MailLib.isDebug())
            Timber.d(">>> New WebDavTransport creation complete");
    }

    @Override
    public void open() throws MessagingException {
        if (K9MailLib.isDebug())
            Timber.d( ">>> open called on WebDavTransport ");

        store.getHttpClient();
    }

    @Override
    public void close() {
    }

    @Override
    public void sendMessage(Message message) throws MessagingException {
        store.sendMessages(Collections.singletonList(message));
    }

    public void checkSettings() throws MessagingException {
        store.checkSettings();
    }
}
