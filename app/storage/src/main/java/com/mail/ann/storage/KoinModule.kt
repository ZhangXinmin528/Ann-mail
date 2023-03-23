package com.mail.ann.storage

import com.mail.ann.mailstore.MessageStoreFactory
import com.mail.ann.mailstore.SchemaDefinitionFactory
import com.mail.ann.notification.NotificationStoreProvider
import com.mail.ann.storage.messages.K9MessageStoreFactory
import com.mail.ann.storage.notifications.K9NotificationStoreProvider
import org.koin.dsl.module

val storageModule = module {
    single<SchemaDefinitionFactory> { K9SchemaDefinitionFactory() }
    single<MessageStoreFactory> {
        K9MessageStoreFactory(localStoreProvider = get(), storageManager = get(), basicPartInfoExtractor = get())
    }
    single<NotificationStoreProvider> {
        K9NotificationStoreProvider(localStoreProvider = get())
    }
}
