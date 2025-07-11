package com.yapp.yapp.record.domain

import com.yapp.yapp.common.TimeProvider
import java.time.Duration

class Pace(
    val millsPerKm: Long, // 1km 당 소요 시간(밀리초)
) {
    companion object {
        fun calculatePace(
            distanceMeter: Double,
            duration: Duration,
        ): Long {
            if (distanceMeter <= 0.0 || duration.isZero) {
                return 0L
            }
            val distanceKm = distanceMeter / 1000.0

            return (duration.toMillis() / distanceKm).toLong()
        }

        fun calculatePace(
            distanceMeter: Double,
            durationMills: Long,
        ): Long {
            if (distanceMeter <= 0.0 || durationMills == 0L) {
                return 0L
            }
            val distanceKm = distanceMeter / 1000.0

            return (durationMills / distanceKm).toLong()
        }
    }
    constructor() : this(0L)

    constructor(distanceMeter: Double, duration: Duration) :
        this(calculatePace(distanceMeter, duration))

    constructor(distanceMeter: Double, durationMills: Long) :
        this(calculatePace(distanceMeter, durationMills))

    override fun toString(): String {
        val seconds = TimeProvider.millsToSecond(toMills())
        return String.format("%d:%02d /km", seconds / 60, seconds % 60)
    }

    fun toMills(): Long {
        return millsPerKm
    }
}
