package com.mail.ann.ui.messagesource

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val messageSourceModule = module {
    viewModel { MessageHeadersViewModel(messageRepository = get()) }
}
