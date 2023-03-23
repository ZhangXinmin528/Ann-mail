package com.mail.ann.mail.store.imap

interface IdleRefreshManager {
    fun startTimer(timeout: Long, callback: () -> Unit): IdleRefreshTimer
    fun resetTimers()
}

interface IdleRefreshTimer {
    val isWaiting: Boolean
    fun cancel()
}
