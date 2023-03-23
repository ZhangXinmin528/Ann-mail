package com.mail.ann.controller.push

import com.mail.ann.Account
import com.mail.ann.Preferences
import com.mail.ann.backend.BackendManager
import com.mail.ann.controller.MessagingController
import com.mail.ann.mailstore.FolderRepository

internal class AccountPushControllerFactory(
    private val backendManager: BackendManager,
    private val messagingController: MessagingController,
    private val folderRepository: FolderRepository,
    private val preferences: Preferences
) {
    fun create(account: Account): AccountPushController {
        return AccountPushController(
            backendManager,
            messagingController,
            preferences,
            folderRepository,
            account = account
        )
    }
}
