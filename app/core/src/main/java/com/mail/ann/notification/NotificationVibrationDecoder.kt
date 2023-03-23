package com.mail.ann.notification

import com.mail.ann.NotificationVibration
import com.mail.ann.VibratePattern

/**
 * Converts the vibration values read from a `NotificationChannel` into [NotificationVibration].
 */
class NotificationVibrationDecoder {
    fun decode(isVibrationEnabled: Boolean, systemPattern: List<Long>?): NotificationVibration {
        if (systemPattern == null || systemPattern.size < 2 || systemPattern.size % 2 != 0) {
            return NotificationVibration(isVibrationEnabled, VibratePattern.Default, repeatCount = 1)
        }

        val systemPatternArray = systemPattern.toLongArray()
        val repeatCount = systemPattern.size / 2
        val pattern = VibratePattern.values()
            .firstOrNull { vibratePattern ->
                val testPattern = NotificationVibration.getSystemPattern(vibratePattern, repeatCount)

                testPattern.contentEquals(systemPatternArray)
            } ?: VibratePattern.Default

        return NotificationVibration(isVibrationEnabled, pattern, repeatCount)
    }
}
