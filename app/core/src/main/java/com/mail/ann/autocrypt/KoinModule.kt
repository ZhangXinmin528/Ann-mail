package com.mail.ann.autocrypt

import org.koin.dsl.module

val autocryptModule = module {
    single { AutocryptTransferMessageCreator(get()) }
    single { AutocryptDraftStateHeaderParser() }
}
