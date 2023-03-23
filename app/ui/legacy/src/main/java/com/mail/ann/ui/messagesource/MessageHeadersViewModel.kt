package com.mail.ann.ui.messagesource

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mail.ann.controller.MessageReference
import com.mail.ann.mail.Header
import com.mail.ann.mailstore.MessageRepository
import com.mail.ann.ui.base.loader.LoaderState
import com.mail.ann.ui.base.loader.liveDataLoader

private typealias MessageHeaderState = LoaderState<List<Header>>

class MessageHeadersViewModel(private val messageRepository: MessageRepository) : ViewModel() {
    private var messageHeaderLiveData: LiveData<MessageHeaderState>? = null

    fun loadHeaders(messageReference: MessageReference): LiveData<MessageHeaderState> {
        return messageHeaderLiveData ?: loadMessageHeader(messageReference).also { messageHeaderLiveData = it }
    }

    private fun loadMessageHeader(messageReference: MessageReference): LiveData<MessageHeaderState> {
        return liveDataLoader {
            messageRepository.getHeaders(messageReference)
        }
    }
}
