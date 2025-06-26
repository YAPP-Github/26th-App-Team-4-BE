package com.yapp.yapp.record.api.response

import com.yapp.yapp.running.domain.Pace.Companion.averagePace
import java.time.Duration

data class RecordListResponse(
    val userId: Long,
    val records: List<RecordResponse>,
    val recordCount: Int,
    val totalDistance: Double,
    val totalTime: Duration,
    val totalCalories: Int,
    val averageSpeed: Double,
    val averagePace: Duration,
) {
    constructor(userId: Long, records: List<RecordResponse>) : this(
        userId = userId,
        records = records,
        recordCount = records.size,
        totalDistance = records.sumOf { it.totalDistance },
        totalTime = records.fold(Duration.ZERO) { acc, record -> acc.plus(record.totalTime) },
        totalCalories = records.sumOf { it.totalCalories },
        averageSpeed = if (records.isEmpty()) 0.0 else records.map { it.averageSpeed }.average(),
        averagePace = if (records.isEmpty()) Duration.ZERO else records.map { it.averagePace }.averagePace().pacePerKm,
    )
}
