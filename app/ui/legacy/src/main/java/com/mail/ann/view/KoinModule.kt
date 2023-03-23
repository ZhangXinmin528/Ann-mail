package com.mail.ann.view

import org.koin.dsl.module

val viewModule = module {
    single { WebViewConfigProvider(get()) }
}
