package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.record.domain.record.goal.RunningRecordGoalAchieve
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
    val isPaceGoalAchieved: Boolean,
    val isDistanceGoalAchieved: Boolean,
    val isTimeGoalAchieved: Boolean,
) {
    constructor(runningRecord: RunningRecord, runningPoints: List<RunningPoint>, runningRecordGoalAchieve: RunningRecordGoalAchieve) : this(
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
        isPaceGoalAchieved = runningRecordGoalAchieve.isPaceGoalAchieved,
        isDistanceGoalAchieved = runningRecordGoalAchieve.isDistanceGoalAchieved,
        isTimeGoalAchieved = runningRecordGoalAchieve.isTimeGoalAchieved,
    )
}
