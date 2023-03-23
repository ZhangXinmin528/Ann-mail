package com.mail.ann.ui.endtoend

import androidx.lifecycle.ViewModel

internal class AutocryptKeyTransferViewModel(
    val autocryptSetupMessageLiveEvent: AutocryptSetupMessageLiveEvent,
    val autocryptSetupTransferLiveEvent: AutocryptSetupTransferLiveEvent
) : ViewModel()
