package com.yapp.yapp.running

import java.time.Duration
import kotlin.math.roundToInt

data class Pace(
    val distance: Double,
    val duration: Duration,
) {
    val pacePerKm: Duration
        get() = Duration.ofSeconds((duration.seconds / distance).roundToInt().toLong())

    val averageSpeedKmh: Double
        get() = distance / (duration.seconds / 3600.0)

    override fun toString(): String {
        val minutes = pacePerKm.toMinutes()
        val seconds = pacePerKm.minusMinutes(minutes).seconds
        return String.format("%d:%02d /km", minutes, seconds)
    }

    companion object {
        fun fromSpeedKmh(speedKmh: Double): Pace {
            val distance = 1.0 // 1 kilometer
            val durationHours = 1.0 / speedKmh
            val durationSeconds = (durationHours * 3600).roundToInt()
            return Pace(distance, Duration.ofSeconds(durationSeconds.toLong()))
        }
    }
}
