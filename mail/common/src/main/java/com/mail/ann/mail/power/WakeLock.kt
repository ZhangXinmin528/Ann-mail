package com.mail.ann.mail.power

interface WakeLock {
    fun acquire(timeout: Long)
    fun acquire()
    fun setReferenceCounted(counted: Boolean)
    fun release()
}
