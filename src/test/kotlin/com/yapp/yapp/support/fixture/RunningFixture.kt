package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.point.RunningPointRepository
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.record.domain.record.RunningRecordRepository
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.OffsetDateTime

@Component
class RunningFixture(
    private val runningRecordRepository: RunningRecordRepository,
    private val runningPointRepository: RunningPointRepository,
) {
    fun createRunningRecord(
        userId: Long = 0L,
        totalDistance: Double = 50.0,
        averageSpeedKmh: Double = 12.0,
        totalCalories: Int = 180,
        totalTime: Duration = Duration.ofSeconds(9),
        startAt: OffsetDateTime = TimeProvider.parse("2025-06-17T17:00:00.000+09:00"),
        averagePace: Pace = Pace(distance = totalDistance, duration = totalTime),
    ): RunningRecord {
        // 1) 러닝 레코드 저장
        val runningRecord =
            runningRecordRepository.save(
                RunningRecord(
                    userId = userId,
                    totalDistance = totalDistance,
                    averageSpeed = averageSpeedKmh,
                    totalCalories = totalCalories,
                    totalTime = totalTime,
                    startAt = startAt,
                    averagePace = averagePace,
                ),
            )

        // 계산 보조 값
        val totalSeconds = totalTime.seconds
        val speedMetersPerSecond = averageSpeedKmh * 1000 / 3600
        val caloriesPerSecond = totalCalories.toDouble() / totalSeconds
        val heartRateStart = 140
        val heartRateEnd = 160
        val heartRateRange = heartRateEnd - heartRateStart

        // 2) 1초 단위 러닝 포인트 생성 및 저장
        (1..totalSeconds).forEach { second ->
            val elapsed = Duration.ofSeconds(second)
            // km/h → m로 환산: (km/h) * (초/3600) * 1000
            val distanceSoFar = averageSpeedKmh * (second / 3600.0) * 1000
            val caloriesSoFar = (caloriesPerSecond * second).toInt()
            val heartRate = heartRateStart + ((heartRateRange * second) / totalSeconds).toInt()

            val runningPoint =
                RunningPoint(
                    runningRecord = runningRecord,
                    orderNo = second,
                    lat = 37.5665 + 0.00001 * second,
                    lon = 126.9780 + 0.00001 * second,
                    speedKmh = averageSpeedKmh,
                    distance = distanceSoFar,
                    pace = averagePace,
                    heartRate = heartRate,
                    calories = caloriesSoFar,
                    totalRunningTime = elapsed,
                    totalRunningDistance = distanceSoFar,
                    timeStamp = startAt.plusSeconds(second),
                )
            runningPointRepository.save(runningPoint)
        }

        // 3) 저장된 러닝 레코드 반환
        return runningRecord
    }
}
