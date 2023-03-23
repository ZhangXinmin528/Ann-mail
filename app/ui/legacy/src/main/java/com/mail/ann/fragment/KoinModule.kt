package com.mail.ann.fragment

import org.koin.dsl.module

val fragmentModule = module {
    single { SortTypeToastProvider() }
}
