package com.mail.ann.resources

import com.mail.ann.CoreResourceProvider
import com.mail.ann.autocrypt.AutocryptStringProvider
import org.koin.dsl.module

val resourcesModule = module {
    single<CoreResourceProvider> { K9CoreResourceProvider(get()) }
    single<AutocryptStringProvider> { K9AutocryptStringProvider(get()) }
}
