package com.mail.ann.ui.endtoend

import android.app.PendingIntent
import com.mail.ann.Account
import com.mail.ann.controller.MessagingController
import com.mail.ann.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AutocryptSetupTransferLiveEvent(
    private val messagingController: MessagingController
) : SingleLiveEvent<AutocryptSetupTransferResult>() {

    fun sendMessageAsync(account: Account, setupMsg: AutocryptSetupMessage) {
        GlobalScope.launch(Dispatchers.Main) {
            val setupMessage = async(Dispatchers.IO) {
                messagingController.sendMessageBlocking(account, setupMsg.setupMessage)
            }

            delay(2000)

            try {
                setupMessage.await()
                value = AutocryptSetupTransferResult.Success(setupMsg.showTransferCodePi)
            } catch (e: Exception) {
                value = AutocryptSetupTransferResult.Failure(e)
            }
        }
    }
}

sealed class AutocryptSetupTransferResult {
    data class Success(val showTransferCodePi: PendingIntent) : AutocryptSetupTransferResult()
    data class Failure(val exception: Exception) : AutocryptSetupTransferResult()
}
