package com.mail.ann

/**
 * An interface to provide the current time.
 */
interface Clock {
    val time: Long
}

internal class RealClock : Clock {
    override val time: Long
        get() = System.currentTimeMillis()
}
