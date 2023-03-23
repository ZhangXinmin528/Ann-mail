package com.mail.ann.backend.imap

import com.mail.ann.mail.Flag
import com.mail.ann.mail.store.imap.ImapStore

internal class CommandSearch(private val imapStore: ImapStore) {

    fun search(
        folderServerId: String,
        query: String?,
        requiredFlags: Set<Flag>?,
        forbiddenFlags: Set<Flag>?,
        performFullTextSearch: Boolean
    ): List<String> {
        val folder = imapStore.getFolder(folderServerId)
        try {
            return folder.search(query, requiredFlags, forbiddenFlags, performFullTextSearch)
                .sortedWith(UidReverseComparator())
                .map { it.uid }
        } finally {
            folder.close()
        }
    }
}
