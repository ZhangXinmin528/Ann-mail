package com.mail.ann.search

import org.koin.dsl.module

val searchModule = module {
    single { AccountSearchConditions() }
}
