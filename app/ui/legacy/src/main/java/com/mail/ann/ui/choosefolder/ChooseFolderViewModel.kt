package com.mail.ann.ui.choosefolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mail.ann.Account
import com.mail.ann.Account.FolderMode
import com.mail.ann.mailstore.DisplayFolder
import com.mail.ann.mailstore.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ChooseFolderViewModel(private val folderRepository: FolderRepository) : ViewModel() {
    private val inputFlow = MutableSharedFlow<DisplayMode>(replay = 1)
    private val foldersFlow = inputFlow
        .flatMapLatest { (account, displayMode) ->
            folderRepository.getDisplayFoldersFlow(account, displayMode)
        }

    var currentDisplayMode: FolderMode? = null
        private set

    fun getFolders(): LiveData<List<DisplayFolder>> {
        return foldersFlow.asLiveData()
    }

    fun setDisplayMode(account: Account, displayMode: FolderMode) {
        currentDisplayMode = displayMode
        viewModelScope.launch {
            inputFlow.emit(DisplayMode(account, displayMode))
        }
    }
}

private data class DisplayMode(val account: Account, val displayMode: FolderMode)
