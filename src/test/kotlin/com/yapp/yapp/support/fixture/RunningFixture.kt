package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RunningMetricsCalculator
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
        totalDistance: Double = RunningMetricsCalculator.calculateDistance(speedKmh = 12.0, seconds = 9),
        totalTime: Duration = Duration.ofSeconds(9),
        startAt: OffsetDateTime = TimeProvider.parse("2025-06-17T17:00:00.000+09:00"),
    ): RunningRecord {
        // 1) 러닝 레코드 저장
        val averageSpeedKmh =
            RunningMetricsCalculator.calculateSpeedKmh(
                meterDistance = totalDistance,
                seconds = totalTime.seconds,
            )
        val runningRecord =
            runningRecordRepository.save(
                RunningRecord(
                    userId = userId,
                    startAt = startAt,
                ),
            )

        // 계산 보조 값
        val totalSeconds = totalTime.seconds
        val caloriesPerSecond = 0.22
        val heartRateStart = 140.0
        val heartRateEnd = 160.0
        val heartRateRange = heartRateEnd - heartRateStart

        // 2) 1초 단위 러닝 포인트 생성 및 저장
        (1..totalSeconds).forEach { second ->
            val elapsed = Duration.ofSeconds(second)
            // km/h → m로 환산: (km/h) * (초/3600) * 1000
            val distanceSoFar = averageSpeedKmh * (second / 3600.0) * 1000
            val caloriesSoFar = (caloriesPerSecond * second).toInt()
            val heartRate = heartRateStart.toInt() + ((heartRateRange * second) / totalSeconds).toInt()

            val runningPoint =
                RunningPoint(
                    runningRecord = runningRecord,
                    orderNo = second,
                    lat = 37.5665 + 0.00001 * second,
                    lon = 126.9780 + 0.00001 * second,
                    speedKmh = averageSpeedKmh,
                    distance = distanceSoFar,
                    pace = Pace(distanceMeter = totalDistance, duration = totalTime),
                    heartRate = heartRate,
                    calories = caloriesSoFar,
                    totalRunningTime = elapsed,
                    totalRunningDistance = distanceSoFar,
                    timeStamp = startAt.plusSeconds(second),
                )
            runningPointRepository.save(runningPoint)
        }

        // 3) 저장된 러닝 레코드 반환
        return runningRecordRepository.findById(runningRecord.id).get()
    }
}
