package com.yapp.yapp.running

import kotlin.math.acos
import kotlin.math.cos
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
        return distance
    }

    fun calculateSpeed(
        preRunningPoint: RunningPoint,
        runningPoint: RunningPoint,
    ): Double {
        val distance = runningPoint.distance - preRunningPoint.distance
        val time = runningPoint.timeStamp.toEpochSecond() - preRunningPoint.timeStamp.toEpochSecond()
        return distance / time
    }
}
