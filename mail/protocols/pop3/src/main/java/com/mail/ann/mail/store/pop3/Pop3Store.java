package com.mail.ann.mail.store.pop3;


import java.util.HashMap;
import java.util.Map;

import com.mail.ann.mail.AuthType;
import com.mail.ann.mail.ConnectionSecurity;
import com.mail.ann.mail.MessagingException;
import com.mail.ann.mail.ServerSettings;
import com.mail.ann.mail.ssl.TrustedSocketFactory;
import org.jetbrains.annotations.NotNull;


public class Pop3Store {
    private final TrustedSocketFactory trustedSocketFactory;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String clientCertificateAlias;
    private final AuthType authType;
    private final ConnectionSecurity connectionSecurity;

    private Map<String, Pop3Folder> mFolders = new HashMap<>();

    public Pop3Store(ServerSettings serverSettings, TrustedSocketFactory socketFactory) {
        trustedSocketFactory = socketFactory;
        host = serverSettings.host;
        port = serverSettings.port;
        connectionSecurity = serverSettings.connectionSecurity;
        username = serverSettings.username;
        password = serverSettings.password;
        clientCertificateAlias = serverSettings.clientCertificateAlias;
        authType = serverSettings.authenticationType;
    }

    @NotNull
    public Pop3Folder getFolder(String name) {
        Pop3Folder folder = mFolders.get(name);
        if (folder == null) {
            folder = new Pop3Folder(this, name);
            mFolders.put(folder.getServerId(), folder);
        }
        return folder;
    }

    public void checkSettings() throws MessagingException {
        Pop3Folder folder = new Pop3Folder(this, Pop3Folder.INBOX);
        try {
            folder.open();
            folder.requestUidl();
        }
        finally {
            folder.close();
        }
    }

    public Pop3Connection createConnection() throws MessagingException {
        return new Pop3Connection(new StorePop3Settings(), trustedSocketFactory);
    }

    private class StorePop3Settings implements Pop3Settings {
        @Override
        public String getHost() {
            return host;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public ConnectionSecurity getConnectionSecurity() {
            return connectionSecurity;
        }

        @Override
        public AuthType getAuthType() {
            return authType;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getClientCertificateAlias() {
            return clientCertificateAlias;
        }
    }

}
