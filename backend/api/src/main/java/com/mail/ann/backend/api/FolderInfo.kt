package com.mail.ann.backend.api

import com.mail.ann.mail.FolderType

data class FolderInfo(val serverId: String, val name: String, val type: FolderType)
