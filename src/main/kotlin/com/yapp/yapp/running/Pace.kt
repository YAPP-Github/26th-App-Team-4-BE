package com.yapp.yapp.running

import java.time.Duration
import kotlin.math.roundToInt

class Pace(
    val pacePerKm: Duration,
) {
    constructor(distance: Double, duration: Duration) :
        this(pacePerKm = Duration.ofSeconds((duration.seconds / distance).roundToInt().toLong()))

    override fun toString(): String {
        val minutes = pacePerKm.toMinutes()
        val seconds = pacePerKm.minusMinutes(minutes).seconds
        return String.format("%d:%02d /km", minutes, seconds)
    }
}
