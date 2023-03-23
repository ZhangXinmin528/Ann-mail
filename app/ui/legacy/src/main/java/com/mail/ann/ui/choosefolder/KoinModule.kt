package com.mail.ann.ui.choosefolder

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chooseFolderUiModule = module {
    viewModel { ChooseFolderViewModel(get()) }
}
