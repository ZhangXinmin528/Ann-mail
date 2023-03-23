package com.mail.ann.ui.messagelist

import android.content.ContentResolver
import com.mail.ann.Preferences
import kotlinx.coroutines.CoroutineScope

class MessageListLiveDataFactory(
    private val messageListLoader: MessageListLoader,
    private val preferences: Preferences,
    private val contentResolver: ContentResolver
) {
    fun create(coroutineScope: CoroutineScope, config: MessageListConfig): MessageListLiveData {
        return MessageListLiveData(messageListLoader, preferences, contentResolver, coroutineScope, config)
    }
}
