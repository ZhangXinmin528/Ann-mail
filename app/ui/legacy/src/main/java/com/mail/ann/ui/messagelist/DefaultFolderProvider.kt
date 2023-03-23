package com.mail.ann.ui.messagelist

import com.mail.ann.Account

/**
 * Decides which folder to display when an account is selected.
 */
class DefaultFolderProvider {
    fun getDefaultFolder(account: Account): Long {
        // Until the UI can handle the case where no remote folders have been fetched yet, we fall back to the Outbox
        // which should always exist.
        return account.autoExpandFolderId ?: account.inboxFolderId ?: account.outboxFolderId ?: error("Outbox missing")
    }
}
