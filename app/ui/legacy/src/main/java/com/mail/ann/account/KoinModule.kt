package com.mail.ann.account

import org.koin.dsl.module

val accountModule = module {
    factory {
        AccountRemover(
            localStoreProvider = get(),
            messagingController = get(),
            backendManager = get(),
            localKeyStoreManager = get(),
            preferences = get()
        )
    }
    factory { BackgroundAccountRemover(get()) }
    factory { AccountCreator(get(), get()) }
}
