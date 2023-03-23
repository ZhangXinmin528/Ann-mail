package com.mail.ann.backends

import app.k9mail.dev.developmentBackends
import app.k9mail.dev.developmentModuleAdditions
import com.mail.ann.backend.BackendManager
import com.mail.ann.backend.imap.BackendIdleRefreshManager
import com.mail.ann.backend.imap.SystemAlarmManager
import com.mail.ann.helper.DefaultTrustedSocketFactory
import com.mail.ann.mail.store.imap.IdleRefreshManager
import com.mail.ann.mail.store.webdav.SniHostSetter
import org.koin.dsl.module

val backendsModule = module {
    single {
        BackendManager(
            mapOf(
                "imap" to get<ImapBackendFactory>(),
                "pop3" to get<Pop3BackendFactory>(),
                "webdav" to get<WebDavBackendFactory>()
            ) + developmentBackends()
        )
    }
    single {
        ImapBackendFactory(
            accountManager = get(),
            powerManager = get(),
            idleRefreshManager = get(),
            backendStorageFactory = get(),
            trustedSocketFactory = get(),
            context = get()
        )
    }
    single<SystemAlarmManager> { AndroidAlarmManager(context = get(), alarmManager = get()) }
    single<IdleRefreshManager> { BackendIdleRefreshManager(alarmManager = get()) }
    single { Pop3BackendFactory(get(), get()) }
    single {
        WebDavBackendFactory(
            backendStorageFactory = get(),
            trustManagerFactory = get(),
            sniHostSetter = get(),
            folderRepository = get()
        )
    }
    single {
        SniHostSetter { factory, socket, hostname ->
            DefaultTrustedSocketFactory.setSniHost(factory, socket, hostname)
        }
    }

    developmentModuleAdditions()
}
