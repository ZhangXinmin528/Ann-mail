package com.mail.ann

import android.app.Application
import com.mail.ann.backend.BackendManager
import com.mail.ann.controller.ControllerExtension
import com.mail.ann.crypto.EncryptionExtractor
import com.mail.ann.notification.NotificationActionCreator
import com.mail.ann.notification.NotificationResourceProvider
import com.mail.ann.notification.NotificationStrategy
import com.mail.ann.preferences.InMemoryStoragePersister
import com.mail.ann.preferences.StoragePersister
import com.mail.ann.storage.storageModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.mockito.kotlin.mock

class TestApp : Application() {
    override fun onCreate() {
        Core.earlyInit()

        super.onCreate()
        DI.start(this, coreModules + storageModule + testModule)

        K9.init(this)
        Core.init(this)
    }
}

val testModule = module {
    single { AppConfig(emptyList()) }
    single { mock<CoreResourceProvider>() }
    single { mock<EncryptionExtractor>() }
    single<StoragePersister> { InMemoryStoragePersister() }
    single { mock<BackendManager>() }
    single { mock<NotificationResourceProvider>() }
    single { mock<NotificationActionCreator>() }
    single { mock<NotificationStrategy>() }
    single(named("controllerExtensions")) { emptyList<ControllerExtension>() }
}
