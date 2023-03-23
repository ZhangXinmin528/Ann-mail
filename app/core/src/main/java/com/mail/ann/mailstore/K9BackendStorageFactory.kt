package com.mail.ann.mailstore

import com.mail.ann.Account
import com.mail.ann.Preferences

class K9BackendStorageFactory(
    private val preferences: Preferences,
    private val folderRepository: FolderRepository,
    private val messageStoreManager: MessageStoreManager,
    private val specialFolderSelectionStrategy: SpecialFolderSelectionStrategy,
    private val saveMessageDataCreator: SaveMessageDataCreator
) {
    fun createBackendStorage(account: Account): K9BackendStorage {
        val messageStore = messageStoreManager.getMessageStore(account)
        val folderSettingsProvider = FolderSettingsProvider(preferences, account)
        val specialFolderUpdater = SpecialFolderUpdater(
            preferences,
            folderRepository,
            specialFolderSelectionStrategy,
            account
        )
        val specialFolderListener = SpecialFolderBackendFoldersRefreshListener(specialFolderUpdater)
        val autoExpandFolderListener = AutoExpandFolderBackendFoldersRefreshListener(preferences, account, folderRepository)
        val listeners = listOf(specialFolderListener, autoExpandFolderListener)
        return K9BackendStorage(messageStore, folderSettingsProvider, saveMessageDataCreator, listeners)
    }
}
