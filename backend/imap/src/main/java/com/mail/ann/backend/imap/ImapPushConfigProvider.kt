package com.mail.ann.backend.imap

import kotlinx.coroutines.flow.Flow

interface ImapPushConfigProvider {
    val maxPushFoldersFlow: Flow<Int>
    val idleRefreshMinutesFlow: Flow<Int>
}
