package com.yapp.yapp.running.domain

import com.yapp.yapp.running.domain.point.RunningPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
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
        val preMs = preRunningPoint.timeStamp.toInstant().toEpochMilli()
        val curMs = runningPoint.timeStamp.toInstant().toEpochMilli()
        val deltaMs = curMs - preMs

        if (distance == 0.0 || deltaMs <= 0) return 0.0

        val timeSeconds = deltaMs / 1000.0

        return (distance / timeSeconds).roundTo()
    }

    fun Double.roundTo(decimals: Int = 3): Double {
        val factor = 10.0.pow(decimals)
        return round(this * factor) / factor
    }
}
