package com.mail.ann.mailstore

import com.mail.ann.mail.FolderClass

data class FolderSettings(
    val visibleLimit: Int,
    val displayClass: FolderClass,
    val syncClass: FolderClass,
    val notifyClass: FolderClass,
    val pushClass: FolderClass,
    val inTopGroup: Boolean,
    val integrate: Boolean,
)
