package com.mail.ann.ui.messagelist

import com.mail.ann.FontSizes

data class MessageListAppearance(
    val fontSizes: FontSizes,
    val previewLines: Int,
    val stars: Boolean,
    val senderAboveSubject: Boolean,
    val showContactPicture: Boolean,
    val showingThreadedList: Boolean,
    val backGroundAsReadIndicator: Boolean,
    val showAccountChip: Boolean
)
