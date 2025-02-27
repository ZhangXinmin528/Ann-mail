package com.mail.ann.widget.unread

import android.content.Context
import android.content.Intent
import com.mail.ann.Account
import com.mail.ann.Preferences
import com.mail.ann.R
import com.mail.ann.activity.MessageList
import com.mail.ann.controller.MessagingController
import com.mail.ann.mailstore.FolderRepository
import com.mail.ann.search.LocalSearch
import com.mail.ann.search.SearchAccount
import com.mail.ann.ui.folders.FolderNameFormatterFactory
import com.mail.ann.ui.messagelist.DefaultFolderProvider
import timber.log.Timber

class UnreadWidgetDataProvider(
    private val context: Context,
    private val preferences: Preferences,
    private val messagingController: MessagingController,
    private val defaultFolderProvider: DefaultFolderProvider,
    private val folderRepository: FolderRepository,
    private val folderNameFormatterFactory: FolderNameFormatterFactory
) {
    fun loadUnreadWidgetData(configuration: UnreadWidgetConfiguration): UnreadWidgetData? = with(configuration) {
        @Suppress("CascadeIf")
        if (SearchAccount.UNIFIED_INBOX == accountUuid) {
            loadSearchAccountData(configuration)
        } else if (folderId != null) {
            loadFolderData(configuration)
        } else {
            loadAccountData(configuration)
        }
    }

    private fun loadSearchAccountData(configuration: UnreadWidgetConfiguration): UnreadWidgetData {
        val searchAccount = getSearchAccount(configuration.accountUuid)
        val title = searchAccount.name ?: searchAccount.email
        val unreadCount = messagingController.getUnreadMessageCount(searchAccount)
        val clickIntent = MessageList.intentDisplaySearch(context, searchAccount.relatedSearch, false, true, true)

        return UnreadWidgetData(configuration, title, unreadCount, clickIntent)
    }

    private fun getSearchAccount(accountUuid: String): SearchAccount = when (accountUuid) {
        SearchAccount.UNIFIED_INBOX -> SearchAccount.createUnifiedInboxAccount()
        else -> throw AssertionError("SearchAccount expected")
    }

    private fun loadAccountData(configuration: UnreadWidgetConfiguration): UnreadWidgetData? {
        val account = preferences.getAccount(configuration.accountUuid) ?: return null
        val title = account.displayName
        val unreadCount = messagingController.getUnreadMessageCount(account)
        val clickIntent = getClickIntentForAccount(account)

        return UnreadWidgetData(configuration, title, unreadCount, clickIntent)
    }

    private fun getClickIntentForAccount(account: Account): Intent {
        val folderId = defaultFolderProvider.getDefaultFolder(account)
        return getClickIntentForFolder(account, folderId)
    }

    private fun loadFolderData(configuration: UnreadWidgetConfiguration): UnreadWidgetData? {
        val accountUuid = configuration.accountUuid
        val account = preferences.getAccount(accountUuid) ?: return null
        val folderId = configuration.folderId ?: return null

        val accountName = account.displayName
        val folderDisplayName = getFolderDisplayName(account, folderId)
        val title = context.getString(R.string.unread_widget_title, accountName, folderDisplayName)

        val unreadCount = messagingController.getFolderUnreadMessageCount(account, folderId)

        val clickIntent = getClickIntentForFolder(account, folderId)

        return UnreadWidgetData(configuration, title, unreadCount, clickIntent)
    }

    private fun getFolderDisplayName(account: Account, folderId: Long): String {
        val folder = folderRepository.getFolder(account, folderId)
        return if (folder != null) {
            val folderNameFormatter = folderNameFormatterFactory.create(context)
            folderNameFormatter.displayName(folder)
        } else {
            Timber.e("Error loading folder for account %s, folder ID: %d", account, folderId)
            ""
        }
    }

    private fun getClickIntentForFolder(account: Account, folderId: Long): Intent {
        val search = LocalSearch()
        search.addAllowedFolder(folderId)
        search.addAccountUuid(account.uuid)

        val clickIntent = MessageList.intentDisplaySearch(context, search, false, true, true)
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        return clickIntent
    }
}
