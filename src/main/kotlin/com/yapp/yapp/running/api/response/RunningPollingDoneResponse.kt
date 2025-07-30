package com.yapp.yapp.running.api.response

import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.OffsetDateTime

data class RunningPollingDoneResponse(
    val recordId: Long,
    val totalRunningDistance: Double,
    val totalRunningTime: Long,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averagePace: Long,
) {
    constructor(runningRecord: RunningRecord) : this(
        runningRecord.id,
        runningRecord.totalDistance,
        runningRecord.totalTime,
        runningRecord.totalCalories,
        runningRecord.startAt,
        runningRecord.averagePace.toMills(),
    )
}
