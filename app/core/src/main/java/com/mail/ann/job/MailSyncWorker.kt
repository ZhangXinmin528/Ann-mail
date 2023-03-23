package com.mail.ann.job

import android.content.ContentResolver
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mail.ann.Account
import com.mail.ann.Ann
import com.mail.ann.Preferences
import com.mail.ann.controller.MessagingController
import com.mail.ann.mail.AuthType
import timber.log.Timber

class MailSyncWorker(
    private val messagingController: MessagingController,
    private val preferences: Preferences,
    context: Context,
    parameters: WorkerParameters
) : Worker(context, parameters) {

    override fun doWork(): Result {
        val accountUuid = inputData.getString(EXTRA_ACCOUNT_UUID)
        requireNotNull(accountUuid)

        Timber.d("Executing periodic mail sync for account %s", accountUuid)

        if (isBackgroundSyncDisabled()) {
            Timber.d("Background sync is disabled. Skipping mail sync.")
            return Result.success()
        }

        val account = preferences.getAccount(accountUuid)
        if (account == null) {
            Timber.e("Account %s not found. Can't perform mail sync.", accountUuid)
            return Result.failure()
        }

        if (account.isPeriodicMailSyncDisabled) {
            Timber.d("Periodic mail sync has been disabled for this account. Skipping mail sync.")
            return Result.success()
        }

        if (account.incomingServerSettings.isMissingCredentials) {
            Timber.d("Password for this account is missing. Skipping mail sync.")
            return Result.success()
        }

        if (account.incomingServerSettings.authenticationType == AuthType.XOAUTH2 && account.oAuthState == null) {
            Timber.d("Account requires sign-in. Skipping mail sync.")
            return Result.success()
        }

        val success = messagingController.performPeriodicMailSync(account)

        return if (success) Result.success() else Result.retry()
    }

    private fun isBackgroundSyncDisabled(): Boolean {
        return when (Ann.backgroundOps) {
            Ann.BACKGROUND_OPS.NEVER -> true
            Ann.BACKGROUND_OPS.ALWAYS -> false
            Ann.BACKGROUND_OPS.WHEN_CHECKED_AUTO_SYNC -> !ContentResolver.getMasterSyncAutomatically()
        }
    }

    private val Account.isPeriodicMailSyncDisabled
        get() = automaticCheckIntervalMinutes <= 0

    companion object {
        const val EXTRA_ACCOUNT_UUID = "accountUuid"
    }
}
