package com.mail.ann.ui.managefolders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mail.ann.Account
import com.mail.ann.mailstore.DisplayFolder
import com.mail.ann.mailstore.FolderRepository

class ManageFoldersViewModel(private val folderRepository: FolderRepository) : ViewModel() {
    fun getFolders(account: Account): LiveData<List<DisplayFolder>> {
        return folderRepository.getDisplayFoldersFlow(account).asLiveData()
    }
}
