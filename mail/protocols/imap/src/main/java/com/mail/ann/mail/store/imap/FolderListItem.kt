package com.mail.ann.mail.store.imap

import com.mail.ann.mail.FolderType

data class FolderListItem(
    val serverId: String,
    val name: String,
    val type: FolderType,
    val oldServerId: String?
)
