package com.mail.ann.job

import androidx.work.WorkManager
import com.mail.ann.Account
import com.mail.ann.Preferences
import timber.log.Timber

class K9JobManager(
    private val workManager: WorkManager,
    private val preferences: Preferences,
    private val mailSyncWorkerManager: MailSyncWorkerManager
) {
    fun scheduleAllMailJobs() {
        Timber.v("scheduling all jobs")
        scheduleMailSync()
    }

    fun scheduleMailSync(account: Account) {
        mailSyncWorkerManager.cancelMailSync(account)
        mailSyncWorkerManager.scheduleMailSync(account)
    }

    private fun scheduleMailSync() {
        cancelAllMailSyncJobs()

        preferences.accounts.forEach { account ->
            mailSyncWorkerManager.scheduleMailSync(account)
        }
    }

    private fun cancelAllMailSyncJobs() {
        Timber.v("canceling mail sync job")
        workManager.cancelAllWorkByTag(MailSyncWorkerManager.MAIL_SYNC_TAG)
    }
}
