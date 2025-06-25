package com.yapp.yapp.running.api.response

import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.Duration
import java.time.OffsetDateTime

data class RunningDoneResponse(
    val recordId: Long,
    val totalRunningDistance: Double,
    val totalRunningTime: Duration,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averageSpeed: Double,
    val averagePace: Duration,
) {
    constructor(runningRecord: RunningRecord) : this(
        runningRecord.id,
        runningRecord.totalDistance,
        runningRecord.totalTime,
        runningRecord.totalCalories,
        runningRecord.startAt,
        runningRecord.averageSpeed,
        runningRecord.averagePace.pacePerKm,
    )
}
