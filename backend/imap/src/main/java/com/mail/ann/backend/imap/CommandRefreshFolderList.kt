package com.mail.ann.backend.imap

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.backend.api.FolderInfo
import com.mail.ann.backend.api.updateFolders
import com.mail.ann.mail.FolderType
import com.mail.ann.mail.store.imap.FolderListItem
import com.mail.ann.mail.store.imap.ImapStore

internal class CommandRefreshFolderList(
    private val backendStorage: BackendStorage,
    private val imapStore: ImapStore
) {
    fun refreshFolderList() {
        // TODO: Start using the proper server ID.
        //  For now we still use the old server ID format (decoded, with prefix removed).
        val foldersOnServer = imapStore.getFolders().toLegacyFolderList()
        val oldFolderServerIds = backendStorage.getFolderServerIds()

        backendStorage.updateFolders {
            val foldersToCreate = mutableListOf<FolderInfo>()
            for (folder in foldersOnServer) {
                if (folder.serverId !in oldFolderServerIds) {
                    foldersToCreate.add(FolderInfo(folder.serverId, folder.name, folder.type))
                } else {
                    changeFolder(folder.serverId, folder.name, folder.type)
                }
            }
            createFolders(foldersToCreate)

            val newFolderServerIds = foldersOnServer.map { it.serverId }
            val removedFolderServerIds = oldFolderServerIds - newFolderServerIds
            deleteFolders(removedFolderServerIds)
        }
    }
}

private fun List<FolderListItem>.toLegacyFolderList(): List<LegacyFolderListItem> {
    return this.filterNot { it.oldServerId == null }
        .map { LegacyFolderListItem(it.oldServerId!!, it.name, it.type) }
}

private data class LegacyFolderListItem(
    val serverId: String,
    val name: String,
    val type: FolderType
)
