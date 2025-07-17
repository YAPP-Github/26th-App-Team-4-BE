package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.OffsetDateTime

data class RunningRecordResponse(
    val recordId: Long,
    val userId: Long,
    val runningPoints: List<RunningPointResponse>,
    val segments: List<SegmentResponse>,
    val totalDistance: Double,
    val totalTime: Long,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averageSpeed: Double,
    val averagePace: Long,
) {
    constructor(runningRecord: RunningRecord, runningPoints: List<RunningPoint>) : this(
        recordId = runningRecord.id,
        userId = runningRecord.userId,
        runningPoints = runningPoints.map { RunningPointResponse(it) },
        segments = SegmentListResponse.of(runningPoints).segmentList,
        totalDistance = runningRecord.totalDistance,
        totalTime = runningRecord.totalTime,
        totalCalories = runningRecord.totalCalories,
        startAt = runningRecord.startAt,
        averageSpeed = runningRecord.averageSpeed,
        averagePace = runningRecord.averagePace.toMills(),
    )
}
