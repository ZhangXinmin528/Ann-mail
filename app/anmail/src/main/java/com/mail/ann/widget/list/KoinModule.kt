package com.mail.ann.widget.list

import org.koin.dsl.module

val messageListWidgetModule = module {
    single { MessageListWidgetUpdateListener(get()) }
}
