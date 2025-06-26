package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.running.domain.Pace
import java.time.Duration
import java.time.OffsetDateTime

data class RecordResponse(
    val recordId: Long,
    val userId: Long,
    val totalDistance: Double,
    val totalTime: Duration,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averageSpeed: Double,
    val averagePace: Pace,
) {
    constructor(runningRecord: RunningRecord) : this(
        recordId = runningRecord.id,
        userId = runningRecord.userId,
        totalDistance = runningRecord.totalDistance,
        totalTime = runningRecord.totalTime,
        totalCalories = runningRecord.totalCalories,
        startAt = runningRecord.startAt,
        averageSpeed = runningRecord.averageSpeed,
        averagePace = runningRecord.averagePace,
    )
}
