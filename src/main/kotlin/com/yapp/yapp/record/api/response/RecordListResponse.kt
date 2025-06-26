package com.yapp.yapp.record.api.response

import java.time.Duration

fun List<Duration>.average(): Duration =
    if (isEmpty()) {
        Duration.ZERO
    } else {
        fold(Duration.ZERO) { acc, duration -> acc.plus(duration) }.dividedBy(size.toLong())
    }

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
        averagePace = records.map { it.averagePace }.average(),
    )
}
