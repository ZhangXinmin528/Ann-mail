package com.mail.ann.mail.store.imap;

import java.io.IOException;

interface UntaggedHandler {
    void handleAsyncUntaggedResponse(ImapResponse response) throws IOException;
}
