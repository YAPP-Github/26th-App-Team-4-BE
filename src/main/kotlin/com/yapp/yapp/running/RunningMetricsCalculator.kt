package com.yapp.yapp.running

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object RunningMetricsCalculator {
    fun calculateDistance(
        preRunningPoint: RunningPoint,
        runningPoint: RunningPoint,
    ): Double {
        val r = 6371000.0

        val lat1Rad = Math.toRadians(preRunningPoint.lat)
        val lon1Rad = Math.toRadians(preRunningPoint.lon)
        val lat2Rad = Math.toRadians(runningPoint.lat)
        val lon2Rad = Math.toRadians(runningPoint.lon)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a =
            sin(dLat / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = r * c
        return distance.roundTo()
    }

    fun calculateSpeed(
        preRunningPoint: RunningPoint,
        runningPoint: RunningPoint,
    ): Double {
        val distance = calculateDistance(preRunningPoint, runningPoint)
        val time = runningPoint.timeStamp.toEpochSecond() - preRunningPoint.timeStamp.toEpochSecond()
        if (distance == 0.0 || time == 0L) return 0.0
        return (distance / time).roundTo()
    }

    fun Double.roundTo(decimals: Int = 3): Double {
        val factor = 10.0.pow(decimals)
        return kotlin.math.round(this * factor) / factor
    }
}
