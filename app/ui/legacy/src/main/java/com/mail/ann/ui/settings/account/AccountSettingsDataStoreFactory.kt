package com.mail.ann.ui.settings.account

import com.mail.ann.Account
import com.mail.ann.Preferences
import com.mail.ann.job.K9JobManager
import com.mail.ann.notification.NotificationChannelManager
import com.mail.ann.notification.NotificationController
import java.util.concurrent.ExecutorService

class AccountSettingsDataStoreFactory(
    private val preferences: Preferences,
    private val jobManager: K9JobManager,
    private val executorService: ExecutorService,
    private val notificationChannelManager: NotificationChannelManager,
    private val notificationController: NotificationController
) {
    fun create(account: Account): AccountSettingsDataStore {
        return AccountSettingsDataStore(
            preferences,
            executorService,
            account,
            jobManager,
            notificationChannelManager,
            notificationController
        )
    }
}
