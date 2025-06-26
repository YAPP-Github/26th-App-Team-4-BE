package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.RunningMetricsCalculator
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.record.RunningRecord
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RunningMetricsCalculatorTest {
    @Test
    fun `거리를 계산한다`() {
        // given
        val runningRecord = RunningRecord()
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
    fun `속도를 계산한다`() {
        // given
        val runningRecord = RunningRecord()
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
        val speed = RunningMetricsCalculator.calculateSpeed(pointA, pointB)

        // then
        Assertions.assertThat(speed).isEqualTo(22.411)
    }
}
