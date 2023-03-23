package com.mail.ann.backend.pop3

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.backend.api.FolderInfo
import com.mail.ann.backend.api.updateFolders
import com.mail.ann.mail.FolderType
import com.mail.ann.mail.store.pop3.Pop3Folder

internal class CommandRefreshFolderList(private val backendStorage: BackendStorage) {
    fun refreshFolderList() {
        val folderServerIds = backendStorage.getFolderServerIds()
        if (Pop3Folder.INBOX !in folderServerIds) {
            backendStorage.updateFolders {
                val inbox = FolderInfo(Pop3Folder.INBOX, Pop3Folder.INBOX, FolderType.INBOX)
                createFolders(listOf(inbox))
            }
        }
    }
}
