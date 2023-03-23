package com.mail.ann.ui.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mail.ann.Account
import com.mail.ann.Ann
import com.mail.ann.controller.MessageCountsProvider
import com.mail.ann.mailstore.DisplayFolder
import com.mail.ann.mailstore.FolderRepository
import com.mail.ann.search.SearchAccount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FoldersViewModel(
    private val folderRepository: FolderRepository,
    private val messageCountsProvider: MessageCountsProvider,
    backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<Account?>(replay = 1)
    private val foldersFlow = inputFlow
        .flatMapLatest { account ->
            if (account == null) {
                flowOf(0 to emptyList())
            } else {
                folderRepository.getDisplayFoldersFlow(account)
                    .map { displayFolders ->
                        account.accountNumber to displayFolders
                    }
            }
        }
        .map { (accountNumber, displayFolders) ->
            FolderList(
                unifiedInbox = createDisplayUnifiedInbox(),
                accountId = accountNumber + 1,
                folders = displayFolders
            )
        }
        .flowOn(backgroundDispatcher)

    private fun createDisplayUnifiedInbox(): DisplayUnifiedInbox? {
        return getUnifiedInboxAccount()?.let { searchAccount ->
            val messageCounts = messageCountsProvider.getMessageCounts(searchAccount)
            DisplayUnifiedInbox(messageCounts.unread, messageCounts.starred)
        }
    }

    private fun getUnifiedInboxAccount(): SearchAccount? {
        return if (Ann.isShowUnifiedInbox) SearchAccount.createUnifiedInboxAccount() else null
    }

    fun getFolderListLiveData(): LiveData<FolderList> {
        return foldersFlow.asLiveData()
    }

    fun loadFolders(account: Account) {
        viewModelScope.launch {
            // When switching accounts we want to remove the old list right away, not keep it until the new list
            // has been loaded.
            inputFlow.emit(null)

            inputFlow.emit(account)
        }
    }
}

data class FolderList(
    val unifiedInbox: DisplayUnifiedInbox?,
    val accountId: Int,
    val folders: List<DisplayFolder>
)

data class DisplayUnifiedInbox(
    val unreadMessageCount: Int,
    val starredMessageCount: Int
)
