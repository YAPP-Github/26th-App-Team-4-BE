package com.yapp.yapp.running

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object RunningMetricsCalculator {
    fun calculateDistance(
        preRunningPoint: RunningPoint,
        runningPoint: RunningPoint,
    ): Double {
        val distance =
            6371 * 1000 * (
                acos(
                    sin(Math.toRadians(preRunningPoint.lat)) * sin(Math.toRadians(runningPoint.lat)) + cos(
                        Math.toRadians(preRunningPoint.lat),
                    ) * cos(Math.toRadians(runningPoint.lat)) * cos(Math.toRadians(runningPoint.lon - preRunningPoint.lon)),
                )
            )
        return distance.roundTo()
    }

    fun calculateSpeed(
        preRunningPoint: RunningPoint,
        runningPoint: RunningPoint,
    ): Double {
        val distance = runningPoint.distance - preRunningPoint.distance
        val time = runningPoint.timeStamp.toEpochSecond() - preRunningPoint.timeStamp.toEpochSecond()
        return (distance / time).roundTo()
    }

    fun Double.roundTo(decimals: Int = 3): Double {
        val factor = 10.0.pow(decimals)
        return kotlin.math.round(this * factor) / factor
    }
}
