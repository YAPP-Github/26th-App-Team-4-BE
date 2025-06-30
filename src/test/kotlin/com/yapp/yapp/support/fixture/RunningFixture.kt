package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.record.domain.record.RunningRecordRepository
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.OffsetDateTime

@Component
class RunningFixture(
    private val runningRecordRepository: RunningRecordRepository,
) {
    fun createRunningRecord(
        userId: Long = 0L,
        totalDistance: Double = 100.0,
        averageSpeed: Double = 126.95000,
        totalCalories: Int = 120,
        totalTime: Duration = Duration.parse("PT0S"),
        startAt: OffsetDateTime = TimeProvider.parse("2025-06-17T17:00:00.000+09:00"),
        averagePace: Pace = Pace(450),
    ) = runningRecordRepository.save(
        RunningRecord(
            userId = userId,
            totalDistance = totalDistance,
            averageSpeed = averageSpeed,
            totalCalories = totalCalories,
            totalTime = totalTime,
            startAt = startAt,
            averagePace = averagePace,
        ),
    )
}
