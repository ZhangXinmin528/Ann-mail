package com.mail.ann

import android.content.Context
import com.mail.ann.helper.Contacts
import com.mail.ann.helper.DefaultTrustedSocketFactory
import com.mail.ann.mail.ssl.LocalKeyStore
import com.mail.ann.mail.ssl.TrustManagerFactory
import com.mail.ann.mail.ssl.TrustedSocketFactory
import com.mail.ann.mailstore.LocalStoreProvider
import com.mail.ann.setup.ServerNameSuggester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {
    single<CoroutineScope>(named("AppCoroutineScope")) { GlobalScope }
    single {
        Preferences(
            storagePersister = get(),
            localStoreProvider = get(),
            accountPreferenceSerializer = get()
        )
    }
    single { get<Context>().resources }
    single { get<Context>().contentResolver }
    single { LocalStoreProvider() }
    single { Contacts.getInstance(get()) }
    single { LocalKeyStore(directoryProvider = get()) }
    single { TrustManagerFactory.createInstance(get()) }
    single { LocalKeyStoreManager(get()) }
    single<TrustedSocketFactory> { DefaultTrustedSocketFactory(get(), get()) }
    single<Clock> { RealClock() }
    factory { ServerNameSuggester() }
    factory { EmailAddressValidator() }
    factory { ServerSettingsSerializer() }
}
