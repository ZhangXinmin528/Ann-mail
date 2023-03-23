package com.mail.ann.preferences

interface StoragePersister {
    fun loadValues(): Storage

    fun createStorageEditor(storageUpdater: StorageUpdater): StorageEditor
}

fun interface StorageUpdater {
    fun updateStorage(updater: (currentStorage: Storage) -> Storage)
}
