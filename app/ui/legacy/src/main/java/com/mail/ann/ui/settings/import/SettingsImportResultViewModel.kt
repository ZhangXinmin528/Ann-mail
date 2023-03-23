package com.mail.ann.ui.settings.import

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mail.ann.helper.SingleLiveEvent

class SettingsImportResultViewModel : ViewModel() {
    private val importResult = SingleLiveEvent<SettingsImportSuccess>()

    val settingsImportResult: LiveData<SettingsImportSuccess> = importResult

    fun setSettingsImportResult() {
        importResult.value = SettingsImportSuccess
    }
}

object SettingsImportSuccess
