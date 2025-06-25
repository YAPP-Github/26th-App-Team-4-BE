package com.yapp.yapp.running.api.response

import com.yapp.yapp.record.domain.point.RunningPoint
import java.time.Duration
import java.time.OffsetDateTime

data class RunningUpdateResponse(
    val id: Long,
    val userId: Long,
    val recordId: Long,
    val ord: Long,
    val lat: Double,
    val lon: Double,
    var speed: Double,
    val distance: Double,
    val pace: Duration,
    val heartRate: Int? = 0,
    val calories: Int,
    val timeStamp: OffsetDateTime,
) {
    constructor(runningPoint: RunningPoint) :
        this(
            id = runningPoint.id,
            userId = runningPoint.runningRecord.id,
            recordId = runningPoint.runningRecord.id,
            ord = runningPoint.ord,
            lat = runningPoint.lat,
            lon = runningPoint.lon,
            speed = runningPoint.speed,
            distance = runningPoint.distance,
            pace = runningPoint.pace.pacePerKm,
            heartRate = runningPoint.heartRate,
            calories = runningPoint.calories,
            timeStamp = runningPoint.timeStamp,
        )
}
