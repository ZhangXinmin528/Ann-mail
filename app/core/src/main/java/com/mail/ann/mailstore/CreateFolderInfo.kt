package com.mail.ann.mailstore

import com.mail.ann.mail.FolderType

data class CreateFolderInfo(
    val serverId: String,
    val name: String,
    val type: FolderType,
    val settings: FolderSettings
)
