package com.mail.ann.account

import com.mail.ann.Account
import com.mail.ann.Core
import com.mail.ann.LocalKeyStoreManager
import com.mail.ann.Preferences
import com.mail.ann.backend.BackendManager
import com.mail.ann.controller.MessagingController
import com.mail.ann.mailstore.LocalStoreProvider
import timber.log.Timber

/**
 * Removes an account and all associated data.
 */
class AccountRemover(
    private val localStoreProvider: LocalStoreProvider,
    private val messagingController: MessagingController,
    private val backendManager: BackendManager,
    private val localKeyStoreManager: LocalKeyStoreManager,
    private val preferences: Preferences
) {

    fun removeAccount(accountUuid: String) {
        val account = preferences.getAccount(accountUuid)
        if (account == null) {
            Timber.w("Can't remove account with UUID %s because it doesn't exist.", accountUuid)
            return
        }

        val accountName = account.toString()
        Timber.v("Removing account '%s'…", accountName)

        removeLocalStore(account)
        messagingController.deleteAccount(account)
        removeBackend(account)

        preferences.deleteAccount(account)

        localKeyStoreManager.deleteCertificates(account)
        Core.setServicesEnabled()

        Timber.v("Finished removing account '%s'.", accountName)
    }

    private fun removeLocalStore(account: Account) {
        try {
            val localStore = localStoreProvider.getInstance(account)
            localStore.delete()
        } catch (e: Exception) {
            Timber.w(e, "Error removing message database for account '%s'", account)

            // Ignore, this may lead to localStores on sd-cards that are currently not inserted to be left
        }

        localStoreProvider.removeInstance(account)
    }

    private fun removeBackend(account: Account) {
        try {
            backendManager.removeBackend(account)
        } catch (e: Exception) {
            Timber.e(e, "Failed to reset remote store for account %s", account)
        }
    }
}
