package com.mail.ann.mailstore

interface BackendFoldersRefreshListener {
    fun onBeforeFolderListRefresh()
    fun onAfterFolderListRefresh()
}
