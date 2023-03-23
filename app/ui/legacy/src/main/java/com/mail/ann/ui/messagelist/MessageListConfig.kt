package com.mail.ann.ui.messagelist

import com.mail.ann.Account.SortType
import com.mail.ann.controller.MessageReference
import com.mail.ann.search.LocalSearch

data class MessageListConfig(
    val search: LocalSearch,
    val showingThreadedList: Boolean,
    val sortType: SortType,
    val sortAscending: Boolean,
    val sortDateAscending: Boolean,
    val activeMessage: MessageReference?
)
