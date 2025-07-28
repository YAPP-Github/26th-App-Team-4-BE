package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.OffsetDateTime

data class RunningRecordResponse(
    val recordId: Long,
    val userId: Long,
    val title: String,
    val runningPoints: List<RunningPointResponse>,
    val segments: List<SegmentResponse>,
    val totalDistance: Double,
    val totalTime: Long,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averagePace: Long,
    val imageUrl: String,
) {
    constructor(runningRecord: RunningRecord, runningPoints: List<RunningPoint>) : this(
        recordId = runningRecord.id,
        userId = runningRecord.user.id,
        title = runningRecord.title,
        runningPoints = runningPoints.map { RunningPointResponse(it) },
        segments = SegmentListResponse.of(runningPoints).segmentList,
        totalDistance = runningRecord.totalDistance,
        totalTime = runningRecord.totalTime,
        totalCalories = runningRecord.totalCalories,
        startAt = runningRecord.startAt,
        averagePace = runningRecord.averagePace.toMills(),
        imageUrl = runningRecord.imageUrl,
    )
}
