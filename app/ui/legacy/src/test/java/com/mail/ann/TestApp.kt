package com.mail.ann

import android.app.Application
import com.mail.ann.preferences.InMemoryStoragePersister
import com.mail.ann.preferences.StoragePersister
import com.mail.ann.storage.storageModule
import org.koin.dsl.module

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
    single<CoreResourceProvider> { TestCoreResourceProvider() }
    single<StoragePersister> { InMemoryStoragePersister() }
}
