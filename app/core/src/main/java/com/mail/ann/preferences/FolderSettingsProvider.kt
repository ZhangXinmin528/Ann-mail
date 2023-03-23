package com.mail.ann.preferences

import com.mail.ann.Account
import com.mail.ann.mail.FolderClass
import com.mail.ann.mailstore.FolderRepository
import com.mail.ann.mailstore.RemoteFolderDetails

class FolderSettingsProvider(private val folderRepository: FolderRepository) {
    fun getFolderSettings(account: Account): List<FolderSettings> {
        return folderRepository.getRemoteFolderDetails(account)
            .filterNot { it.containsOnlyDefaultValues() }
            .map { it.toFolderSettings() }
    }

    private fun RemoteFolderDetails.containsOnlyDefaultValues(): Boolean {
        return isInTopGroup == getDefaultValue("inTopGroup") &&
            isIntegrate == getDefaultValue("integrate") &&
            syncClass == getDefaultValue("syncMode") &&
            displayClass == getDefaultValue("displayMode") &&
            notifyClass == getDefaultValue("notifyMode") &&
            pushClass == getDefaultValue("pushMode")
    }

    private fun getDefaultValue(key: String): Any? {
        val versionedSetting = FolderSettingsDescriptions.SETTINGS[key] ?: error("Key not found: $key")
        val highestVersion = versionedSetting.lastKey()
        val setting = versionedSetting[highestVersion] ?: error("Setting description not found: $key")
        return setting.defaultValue
    }

    private fun RemoteFolderDetails.toFolderSettings(): FolderSettings {
        return FolderSettings(
            folder.serverId,
            isInTopGroup,
            isIntegrate,
            syncClass,
            displayClass,
            notifyClass,
            pushClass
        )
    }
}

data class FolderSettings(
    val serverId: String,
    val isInTopGroup: Boolean,
    val isIntegrate: Boolean,
    val syncClass: FolderClass,
    val displayClass: FolderClass,
    val notifyClass: FolderClass,
    val pushClass: FolderClass
)
