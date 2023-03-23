package com.mail.ann.backend.imap

import com.mail.ann.mail.FolderType
import com.mail.ann.mail.store.imap.FolderListItem
import com.mail.ann.mail.store.imap.ImapFolder
import com.mail.ann.mail.store.imap.ImapStore

class TestImapStore : ImapStore {
    private val folders = mutableMapOf<String, ImapFolder>()

    fun addFolder(serverId: String): TestImapFolder {
        require(!folders.containsKey(serverId)) { "Folder '$serverId' already exists" }

        return TestImapFolder(serverId).also { folder ->
            folders[serverId] = folder
        }
    }

    fun addFolder(folder: ImapFolder) {
        val serverId = folder.serverId
        require(!folders.containsKey(serverId)) { "Folder '$serverId' already exists" }

        folders[serverId] = folder
    }

    override fun getFolder(name: String): ImapFolder {
        return folders[name] ?: error("Folder '$name' not found")
    }

    override fun getFolders(): List<FolderListItem> {
        return folders.values.map { folder ->
            FolderListItem(
                serverId = folder.serverId,
                name = "irrelevant",
                type = FolderType.REGULAR,
                oldServerId = null
            )
        }
    }

    override fun checkSettings() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun closeAllConnections() {
        throw UnsupportedOperationException("not implemented")
    }
}
