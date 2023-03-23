package com.mail.ann.mail.store.imap;

import com.mail.ann.mail.AuthType;
import com.mail.ann.mail.ConnectionSecurity;


/**
 * Settings source for IMAP. Implemented in order to remove coupling between {@link ImapStore} and {@link ImapConnection}.
 */
interface ImapSettings {
    String getHost();

    int getPort();

    ConnectionSecurity getConnectionSecurity();

    AuthType getAuthType();

    String getUsername();

    String getPassword();

    String getClientCertificateAlias();

    boolean useCompression();

    String getPathPrefix();

    void setPathPrefix(String prefix);

    String getPathDelimiter();

    void setPathDelimiter(String delimiter);

    void setCombinedPrefix(String prefix);
}
