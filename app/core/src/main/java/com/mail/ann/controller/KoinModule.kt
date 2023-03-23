package com.mail.ann.controller

import android.content.Context
import com.mail.ann.Preferences
import com.mail.ann.backend.BackendManager
import com.mail.ann.mailstore.LocalStoreProvider
import com.mail.ann.mailstore.MessageStoreManager
import com.mail.ann.mailstore.SaveMessageDataCreator
import com.mail.ann.mailstore.SpecialLocalFoldersCreator
import com.mail.ann.notification.NotificationController
import com.mail.ann.notification.NotificationStrategy
import org.koin.core.qualifier.named
import org.koin.dsl.module

val controllerModule = module {
    single {
        MessagingController(
            get<Context>(),
            get<NotificationController>(),
            get<NotificationStrategy>(),
            get<LocalStoreProvider>(),
            get<MessageCountsProvider>(),
            get<BackendManager>(),
            get<Preferences>(),
            get<MessageStoreManager>(),
            get<SaveMessageDataCreator>(),
            get<SpecialLocalFoldersCreator>(),
            get(named("controllerExtensions"))
        )
    }
    single<MessageCountsProvider> {
        DefaultMessageCountsProvider(
            preferences = get(),
            accountSearchConditions = get(),
            localStoreProvider = get()
        )
    }
}
