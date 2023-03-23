package com.mail.ann.message.quote

import org.koin.dsl.module

val quoteModule = module {
    factory { QuoteDateFormatter() }
    factory { TextQuoteCreator(get(), get()) }
}
