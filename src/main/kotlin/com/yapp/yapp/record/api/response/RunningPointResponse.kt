package com.yapp.yapp.record.api.response

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.converter.PaceConverter
import com.yapp.yapp.record.domain.point.RunningPoint
import jakarta.persistence.Convert
import java.time.Duration
import java.time.OffsetDateTime

data class RunningPointResponse(
    val runningPointId: Long,
    val userId: Long,
    val recordId: Long,
    val orderNo: Long,
    val lat: Double,
    val lon: Double,
    var speed: Double = 0.0,
    var distance: Double = 0.0,
    @Convert(converter = PaceConverter::class)
    var pace: Pace = Pace(0),
    val heartRate: Int? = 0,
    val calories: Int = 0,
    val totalRunningTime: Duration = Duration.ZERO,
    var totalRunningDistance: Double = 0.0,
    val timeStamp: OffsetDateTime = TimeProvider.now(),
) {
    constructor(runningPoint: RunningPoint) : this(
        runningPointId = runningPoint.id,
        userId = runningPoint.runningRecord.userId,
        recordId = runningPoint.runningRecord.id,
        orderNo = runningPoint.orderNo,
        lat = runningPoint.lat,
        lon = runningPoint.lon,
        speed = runningPoint.speed,
        distance = runningPoint.distance,
        pace = runningPoint.pace,
        heartRate = runningPoint.heartRate,
        calories = runningPoint.calories,
        totalRunningTime = runningPoint.totalRunningTime,
        totalRunningDistance = runningPoint.totalRunningDistance,
        timeStamp = runningPoint.timeStamp,
    )
}
