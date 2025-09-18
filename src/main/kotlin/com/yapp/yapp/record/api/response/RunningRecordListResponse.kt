package com.yapp.yapp.record.api.response

import com.yapp.yapp.user.domain.goal.UserGoal

data class RunningRecordListResponse(
    val userId: Long,
    val records: List<RunningRecordSummaryResponse>,
    val recordCount: Int,
    val totalDistance: Double,
    val totalTime: Long,
    val totalCalories: Int,
    val averagePace: Long?,
    val timeGoalAchievedCount: Int?,
    val distanceGoalAchievedCount: Int?,
) {
    constructor(userId: Long, records: List<RunningRecordResponse>, userGoal: UserGoal) : this(
        userId = userId,
        records = records.map { RunningRecordSummaryResponse(it) },
        recordCount = records.size,
        totalDistance = records.sumOf { it.totalDistance },
        totalTime = records.fold(0L) { acc, record -> acc.plus(record.totalTime) },
        totalCalories = records.sumOf { it.totalCalories },
        averagePace = if (records.isEmpty()) null else records.map { it.averagePace }.average().toLong(),
        timeGoalAchievedCount = if (userGoal.hasTimeGoal()) records.count { it.isTimeGoalAchieved } else null,
        distanceGoalAchievedCount = if (userGoal.hasDistanceGoal()) records.count { it.isDistanceGoalAchieved } else null,
    )
}
