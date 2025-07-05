package com.yapp.yapp.record.api.response

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.point.RunningPoint
import java.time.Duration
import java.time.OffsetDateTime

data class RunningPointResponse(
    val id: Long,
    val userId: Long,
    val recordId: Long,
    val orderNo: Long,
    val lat: Double,
    val lon: Double,
    val speed: Double = 0.0,
    val distance: Double = 0.0,
    val pace: Duration = Duration.ZERO,
    val heartRate: Int? = null,
    val calories: Int = 0,
    val totalRunningTime: Duration = Duration.ZERO,
    val totalRunningDistance: Double = 0.0,
    val timeStamp: OffsetDateTime = TimeProvider.now(),
) {
    constructor(runningPoint: RunningPoint) : this(
        id = runningPoint.id,
        userId = runningPoint.runningRecord.userId,
        recordId = runningPoint.runningRecord.id,
        orderNo = runningPoint.orderNo,
        lat = runningPoint.lat,
        lon = runningPoint.lon,
        speed = runningPoint.speedKmh,
        distance = runningPoint.distance,
        pace = runningPoint.pace.pacePerKm,
        heartRate = runningPoint.heartRate,
        calories = runningPoint.calories,
        totalRunningTime = runningPoint.totalRunningTime,
        totalRunningDistance = runningPoint.totalRunningDistance,
        timeStamp = runningPoint.timeStamp,
    )
}
