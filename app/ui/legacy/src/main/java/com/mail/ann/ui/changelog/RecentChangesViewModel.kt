package com.mail.ann.ui.changelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mail.ann.preferences.GeneralSettingsManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class RecentChangesViewModel(
    private val generalSettingsManager: GeneralSettingsManager,
    changeLogManager: ChangeLogManager
) : ViewModel() {
    val shouldShowRecentChangesHint = changeLogManager.changeLogFlow.flatMapLatest { changeLog ->
        if (changeLog.isFirstRun && !changeLog.isFirstRunEver) {
            getShowRecentChangesFlow()
        } else {
            flowOf(false)
        }
    }.asLiveData()

    private fun getShowRecentChangesFlow(): Flow<Boolean> {
        return generalSettingsManager.getSettingsFlow()
            .map { generalSettings -> generalSettings.showRecentChanges }
            .distinctUntilChanged()
    }
}
