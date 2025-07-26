package com.yapp.yapp.record.api.response

data class RunningRecordListResponse(
    val userId: Long,
    val records: List<RunningRecordSummaryResponse>,
    val recordCount: Int,
    val totalDistance: Double,
    val totalTime: Long,
    val totalCalories: Int,
    val averagePace: Long,
    val timeGoalAchievedCount: Int,
    val distanceGoalAchievedCount: Int,
) {
    constructor(userId: Long, records: List<RunningRecordResponse>, timeGoalAchievedCount: Int, distanceGoalAchievedCount: Int) : this(
        userId = userId,
        records = records.map { RunningRecordSummaryResponse(it) },
        recordCount = records.size,
        totalDistance = records.sumOf { it.totalDistance },
        totalTime = records.fold(0L) { acc, record -> acc.plus(record.totalTime) },
        totalCalories = records.sumOf { it.totalCalories },
        averagePace = records.map { it.averagePace }.average().toLong(),
        timeGoalAchievedCount = timeGoalAchievedCount,
        distanceGoalAchievedCount = distanceGoalAchievedCount,
    )
}
