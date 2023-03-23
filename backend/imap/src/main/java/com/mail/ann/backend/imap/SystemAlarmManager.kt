package com.mail.ann.backend.imap

interface SystemAlarmManager {
    fun setAlarm(triggerTime: Long, callback: () -> Unit)
    fun cancelAlarm()
    fun now(): Long
}
