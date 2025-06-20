package com.yapp.yapp.running.api.response

import com.yapp.yapp.running.RunningRecord
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
    constructor(record: RunningRecord) : this(
        record.id,
        record.totalRunningDistance,
        record.totalRunningTime,
        record.totalCalories,
        record.startAt,
        record.averageSpeed,
        record.averagePace.pacePerKm,
    )
}
