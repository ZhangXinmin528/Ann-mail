package com.mail.ann.backend.webdav

import com.mail.ann.backend.api.BackendStorage
import com.mail.ann.backend.api.FolderInfo
import com.mail.ann.backend.api.updateFolders
import com.mail.ann.mail.store.webdav.WebDavStore

internal class CommandRefreshFolderList(
    private val backendStorage: BackendStorage,
    private val webDavStore: WebDavStore
) {
    fun refreshFolderList() {
        val foldersOnServer = webDavStore.personalNamespaces
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
