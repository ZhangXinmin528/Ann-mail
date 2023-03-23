package com.mail.ann.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mail.ann.Account
import com.mail.ann.preferences.AccountManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val accountManager: AccountManager,
    private val coroutineScope: CoroutineScope = GlobalScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val accounts = accountManager.getAccountsFlow().asLiveData()

    fun moveAccount(account: Account, newPosition: Int) {
        coroutineScope.launch(coroutineDispatcher) {
            // Delay saving the account so the animation is not disturbed
            delay(500)

            accountManager.moveAccount(account, newPosition)
        }
    }
}
