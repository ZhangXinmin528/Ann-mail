package com.mail.ann

class TestClock(initialTime: Long = 0L) : Clock {
    override var time: Long = initialTime
}
