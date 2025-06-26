package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.Duration
import java.time.OffsetDateTime

object RunningFixture {
    fun create(
        userId: Long = 0L,
        totalDistance: Double = 100.0,
        averageSpeed: Double = 126.95000,
        totalCalories: Int = 120,
        totalTime: Duration = Duration.parse("PT0S"),
        startAt: OffsetDateTime = TimeProvider.parse("2025-06-17T17:00:00.000+09:00"),
        averagePace: Pace = Pace(450),
    ) = RunningRecord(
        userId = userId,
        totalDistance = totalDistance,
        averageSpeed = averageSpeed,
        totalCalories = totalCalories,
        totalTime = totalTime,
        startAt = startAt,
        averagePace = averagePace,
    )
}
