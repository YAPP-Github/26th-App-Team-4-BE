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
        totalSeconds: Long = 9,
        startAt: OffsetDateTime = TimeProvider.parse("2025-06-17T17:00:00.000+09:00"),
    ): RunningRecord {
        // 1) 러닝 레코드 저장
        val runningRecord =
            runningRecordRepository.save(
                RunningRecord(
                    userId = userId,
                    startAt = startAt,
                ),
            )

        // 계산 보조 값
        val caloriesPerSecond = 0.22
        val heartRateStart = 140.0
        val heartRateEnd = 160.0
        val heartRateRange = heartRateEnd - heartRateStart

        val startLat = 37.5665
        val startLon = 126.9780
        val perDistance =
            RunningMetricsCalculator.calculateDistance(
                startLat,
                startLon,
                startLat + 0.00001,
                startLon + 0.00001,
            )
        val speedKmh = RunningMetricsCalculator.calculateSpeedKmh(perDistance, 1)

        // 2) 1초 단위 러닝 포인트 생성 및 저장
        (0..totalSeconds).forEach { curSecond ->
            val elapsed = TimeProvider.toMills(second = curSecond.toInt())
            // km/h → m로 환산: (km/h) * (초/3600) * 1000
            val caloriesSoFar = (caloriesPerSecond * curSecond).toInt()
            val heartRate = heartRateStart.toInt() + ((heartRateRange * curSecond) / totalSeconds).toInt()
            val toLat = startLat + 0.00001 * curSecond
            val toLon = startLon + 0.00001 * curSecond
            val totalDistance =
                RunningMetricsCalculator.calculateDistance(
                    fromLat = startLat,
                    fromLon = startLon,
                    toLat = toLat,
                    toLon = toLon,
                )

            val runningPoint =
                RunningPoint(
                    runningRecord = runningRecord,
                    orderNo = curSecond,
                    lat = toLat,
                    lon = toLon,
                    speedKmh = speedKmh,
                    distance = if (curSecond.toInt() == 0) 0.0 else perDistance,
                    pace = Pace(distanceMeter = totalDistance, duration = Duration.ofSeconds(curSecond)),
                    heartRate = heartRate,
                    calories = caloriesSoFar,
                    totalRunningTime = elapsed,
                    totalRunningDistance = totalDistance,
                    timeStamp = startAt.plusSeconds(curSecond),
                )
            runningPointRepository.save(runningPoint)
        }
        val savedRunningPoints =
            runningPointRepository.findAllByRunningRecordAndIsDeletedFalseOrderByOrderNoAsc(
                runningRecord,
            )
        runningRecord.updateInfoByRunningPoints(savedRunningPoints)
        return runningRecordRepository.save(runningRecord)
    }
}
