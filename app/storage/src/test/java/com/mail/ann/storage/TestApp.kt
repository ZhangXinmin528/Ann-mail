package com.mail.ann.storage

import android.app.Application
import com.mail.ann.AppConfig
import com.mail.ann.Core
import com.mail.ann.CoreResourceProvider
import com.mail.ann.DI
import com.mail.ann.K9
import com.mail.ann.backend.BackendManager
import com.mail.ann.coreModules
import com.mail.ann.crypto.EncryptionExtractor
import com.mail.ann.preferences.K9StoragePersister
import com.mail.ann.preferences.StoragePersister
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
    single<StoragePersister> { K9StoragePersister(get()) }
    single { mock<BackendManager>() }
}
