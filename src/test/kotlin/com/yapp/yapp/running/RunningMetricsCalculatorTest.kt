package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RunningMetricsCalculator
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.point.RunningPointDao
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.support.BaseServiceTest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration

class RunningMetricsCalculatorTest : BaseServiceTest() {
    @Autowired
    lateinit var runningPointDao: RunningPointDao

    @Test
    fun `거리를 계산한다`() {
        // given
        val user = userFixture.create()
        val runningRecord = RunningRecord(user = user)
        val pointA =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 37.547889,
                lon = 126.997128,
                timeStamp = TimeProvider.parse("2025-06-17T17:00:00+09:00"),
            )
        val pointB =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 35.158874,
                lon = 129.043846,
                timeStamp = TimeProvider.parse("2025-06-17T17:00:01+09:00"),
            )

        // when
        val distance = RunningMetricsCalculator.calculateDistance(pointA, pointB)

        // then
        Assertions.assertThat(distance).isEqualTo(322722.249)
    }

    @Test
    fun `두 좌표 사이의 거리를 계산한다`() {
        // given
        val user = userFixture.create()
        val runningRecord = RunningRecord(user = user)
        val pointA =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 37.56651,
                lon = 126.97801,
                timeStamp = TimeProvider.parse("2025-06-17T17:00:00+09:00"),
            )
        val pointB =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 37.56652,
                lon = 126.97802,
                timeStamp = TimeProvider.parse("2025-06-17T17:00:01+09:00"),
            )

        // when
        val distance = RunningMetricsCalculator.calculateDistance(pointA, pointB)

        // then
        Assertions.assertThat(distance).isCloseTo(1.419, within(0.001))
    }

    @Test
    fun `속도를 계산한다`() {
        // given
        val user = userFixture.create()
        val runningRecord = RunningRecord(user = user)
        val pointA =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 37.547889,
                lon = 126.997128,
                timeStamp = TimeProvider.parse("2025-06-17T17:00:00+09:00"),
            )
        val pointB =
            RunningPoint(
                runningRecord = runningRecord,
                lat = 35.158874,
                lon = 129.043846,
                timeStamp = TimeProvider.parse("2025-06-17T21:00:00+09:00"),
            )

        // when
        val speed = RunningMetricsCalculator.calculateSpeedKmh(pointA, pointB)

        // then
        Assertions.assertThat(speed).isEqualTo(22.411)
    }

    @Test
    fun `fixture를 통해 속도와 거리를 계산한다`() {
        // given
        val user = userFixture.create()
        val runningRecord = runningFixture.createRunningRecord(user)
        val runningPoints = runningPointDao.getAllPointByRunningRecord(runningRecord)
        runningRecord.updateInfoByRunningPoints(runningPoints)

        // when
        var totalDistance = 0.0
        for (i in 0 until runningPoints.size - 1) {
            totalDistance += RunningMetricsCalculator.calculateDistance(runningPoints[i], runningPoints[i + 1])
        }

        val totalTime = Duration.between(runningPoints.first().timeStamp, runningPoints.last().timeStamp)
        val averagePace = Pace(distanceMeter = totalDistance, duration = totalTime)

        // then
        Assertions.assertThat(runningRecord.totalDistance).isCloseTo(totalDistance, within(0.001))
        Assertions.assertThat(runningRecord.averagePace.toMills()).isEqualTo(averagePace.toMills())
    }
}
