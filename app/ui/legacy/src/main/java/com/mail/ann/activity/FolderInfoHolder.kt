package com.mail.ann.activity

import com.mail.ann.Account
import com.mail.ann.mailstore.Folder
import com.mail.ann.mailstore.FolderType
import com.mail.ann.mailstore.LocalFolder
import com.mail.ann.ui.folders.FolderNameFormatter

class FolderInfoHolder(
    private val folderNameFormatter: FolderNameFormatter,
    localFolder: LocalFolder,
    account: Account
) {
    @JvmField val databaseId = localFolder.databaseId
    @JvmField val displayName = getDisplayName(account, localFolder)
    @JvmField var loading = false
    @JvmField var moreMessages = localFolder.hasMoreMessages()

    private fun getDisplayName(account: Account, localFolder: LocalFolder): String {
        val folderId = localFolder.databaseId
        val folder = Folder(
            id = folderId,
            name = localFolder.name,
            type = getFolderType(account, folderId),
            isLocalOnly = localFolder.isLocalOnly
        )
        return folderNameFormatter.displayName(folder)
    }

    companion object {
        @JvmStatic
        fun getFolderType(account: Account, folderId: Long): FolderType {
            return when (folderId) {
                account.inboxFolderId -> FolderType.INBOX
                account.outboxFolderId -> FolderType.OUTBOX
                account.archiveFolderId -> FolderType.ARCHIVE
                account.draftsFolderId -> FolderType.DRAFTS
                account.sentFolderId -> FolderType.SENT
                account.spamFolderId -> FolderType.SPAM
                account.trashFolderId -> FolderType.TRASH
                else -> FolderType.REGULAR
            }
        }
    }
}
