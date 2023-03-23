package com.mail.ann.widget.unread

import android.content.SharedPreferences
import androidx.core.content.edit
import com.mail.ann.Preferences
import com.mail.ann.mailstore.FolderRepository
import com.mail.ann.widget.unread.UnreadWidgetRepository.Companion.PREFS_VERSION
import com.mail.ann.widget.unread.UnreadWidgetRepository.Companion.PREF_VERSION_KEY

internal class UnreadWidgetMigrations(
    private val accountRepository: Preferences,
    private val folderRepository: FolderRepository
) {
    fun upgradePreferences(preferences: SharedPreferences, version: Int) {
        if (version < 2) rewriteFolderNameToFolderId(preferences)

        preferences.setVersion(PREFS_VERSION)
    }

    private fun SharedPreferences.setVersion(version: Int) {
        edit { putInt(PREF_VERSION_KEY, version) }
    }

    private fun rewriteFolderNameToFolderId(preferences: SharedPreferences) {
        val widgetIds = preferences.all.keys
            .filter { it.endsWith(".folder_name") }
            .map { it.split(".")[1] }

        preferences.edit {
            for (widgetId in widgetIds) {
                val accountUuid = preferences.getString("unread_widget.$widgetId", null) ?: continue
                val account = accountRepository.getAccount(accountUuid) ?: continue

                val folderServerId = preferences.getString("unread_widget.$widgetId.folder_name", null)
                if (folderServerId != null) {
                    val folderId = folderRepository.getFolderId(account, folderServerId)
                    putString("unread_widget.$widgetId.folder_id", folderId?.toString())
                }

                remove("unread_widget.$widgetId.folder_name")
            }
        }
    }
}
