package com.mail.ann

import com.mail.ann.auth.createOAuthConfigurationProvider
import com.mail.ann.backends.backendsModule
import com.mail.ann.controller.ControllerExtension
import com.mail.ann.crypto.EncryptionExtractor
import com.mail.ann.crypto.openpgp.OpenPgpEncryptionExtractor
import com.mail.ann.notification.notificationModule
import com.mail.ann.preferences.K9StoragePersister
import com.mail.ann.preferences.StoragePersister
import com.mail.ann.resources.resourcesModule
import com.mail.ann.storage.storageModule
import com.mail.ann.widget.list.MessageListWidgetUpdateListener
import com.mail.ann.widget.list.messageListWidgetModule
import com.mail.ann.widget.unread.UnreadWidgetUpdateListener
import com.mail.ann.widget.unread.unreadWidgetModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val mainAppModule = module {
    single { App.appConfig }
    single {
        MessagingListenerProvider(
            listOf(
                get<UnreadWidgetUpdateListener>(),
                get<MessageListWidgetUpdateListener>()
            )
        )
    }
    single(named("controllerExtensions")) { emptyList<ControllerExtension>() }
    single<EncryptionExtractor> { OpenPgpEncryptionExtractor.newInstance() }
    single<StoragePersister> { K9StoragePersister(get()) }
    single { createOAuthConfigurationProvider() }
}

val appModules = listOf(
    mainAppModule,
    messageListWidgetModule,
    unreadWidgetModule,
    notificationModule,
    resourcesModule,
    backendsModule,
    storageModule
)
