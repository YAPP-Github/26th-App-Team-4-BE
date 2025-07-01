package com.yapp.yapp.record.domain

import java.time.Duration
import kotlin.math.roundToLong

class Pace(
    val pacePerKm: Duration,
) {
    companion object {
        fun List<Pace>.averagePace(): Pace {
            if (isEmpty()) return Pace(Duration.ZERO)

            val totalMillis = sumOf { it.pacePerKm.toMillis() }
            val avgMillis = totalMillis / size
            return Pace(Duration.ofMillis(avgMillis))
        }
    }

    constructor(pacePerKm: Long) : this(Duration.ofSeconds(pacePerKm))

    constructor(distance: Double, duration: Duration) :
        this(
            if (distance == 0.0) {
                Duration.ZERO
            } else {
                Duration.ofSeconds((duration.toMillis() / distance).roundToLong())
            },
        )

    override fun toString(): String {
        val minutes = pacePerKm.toMinutes()
        val seconds = pacePerKm.minusMinutes(minutes).seconds
        return String.format("%d:%02d /km", minutes, seconds)
    }
}
