package com.yapp.yapp.record.api.response

import java.time.OffsetDateTime

data class RunningRecordSummaryResponse(
    val recordId: Long,
    val userId: Long,
    val startAt: OffsetDateTime,
    val averagePace: Long,
    val totalDistance: Double,
    val totalTime: Long,
    val imageUrl: String,
) {
    constructor(runningRecordResponse: RunningRecordResponse) : this(
        recordId = runningRecordResponse.recordId,
        userId = runningRecordResponse.userId,
        startAt = runningRecordResponse.startAt,
        averagePace = runningRecordResponse.averagePace,
        totalDistance = runningRecordResponse.totalDistance,
        totalTime = runningRecordResponse.totalTime,
        imageUrl = runningRecordResponse.imageUrl,
    )
}
