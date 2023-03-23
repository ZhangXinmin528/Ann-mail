package com.mail.ann.mail.store.pop3;


import com.mail.ann.mail.AuthType;
import com.mail.ann.mail.ConnectionSecurity;


interface Pop3Settings {
    String getHost();

    int getPort();

    ConnectionSecurity getConnectionSecurity();

    AuthType getAuthType();

    String getUsername();

    String getPassword();

    String getClientCertificateAlias();
}
