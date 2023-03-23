package com.mail.ann.mail.power

interface PowerManager {
    fun newWakeLock(tag: String): WakeLock
}
