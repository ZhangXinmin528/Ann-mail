package com.mail.ann.mailstore

import com.mail.ann.message.extractors.AttachmentCounter
import com.mail.ann.message.extractors.MessageFulltextCreator
import com.mail.ann.message.extractors.MessagePreviewCreator
import org.koin.dsl.module

val mailStoreModule = module {
    single { FolderRepository(messageStoreManager = get(), accountManager = get()) }
    single { MessageViewInfoExtractorFactory(get(), get(), get()) }
    single { StorageManager.getInstance(get()) }
    single { SearchStatusManager() }
    single { SpecialFolderSelectionStrategy() }
    single {
        K9BackendStorageFactory(
            preferences = get(),
            folderRepository = get(),
            messageStoreManager = get(),
            specialFolderSelectionStrategy = get(),
            saveMessageDataCreator = get()
        )
    }
    factory { SpecialLocalFoldersCreator(preferences = get(), localStoreProvider = get()) }
    single { MessageStoreManager(accountManager = get(), messageStoreFactory = get()) }
    single { MessageRepository(messageStoreManager = get()) }
    factory { MessagePreviewCreator.newInstance() }
    factory { MessageFulltextCreator.newInstance() }
    factory { AttachmentCounter.newInstance() }
    factory {
        SaveMessageDataCreator(
            encryptionExtractor = get(),
            messagePreviewCreator = get(),
            messageFulltextCreator = get(),
            attachmentCounter = get()
        )
    }
}
