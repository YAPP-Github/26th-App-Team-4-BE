package com.yapp.yapp.running

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.Pace.Companion.averagePace
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.Duration

class PaceTest {
    @Test
    fun `페이스를 계산한다`() {
        // given
        val pace1 = Pace(1.0, Duration.parse("PT1S"))
        val pace2 = Pace(2.0, Duration.parse("PT1S"))
        val pace3 = Pace(3.0, Duration.parse("PT1S"))
        val pace4 = Pace(3.1, Duration.parse("PT1S"))

        // when
        // then
        assertAll(
            { Assertions.assertThat(pace1.pacePerKm).isEqualTo(Duration.parse("PT16M40S")) },
            { Assertions.assertThat(pace2.pacePerKm).isEqualTo(Duration.parse("PT8M20S")) },
            { Assertions.assertThat(pace3.pacePerKm).isEqualTo(Duration.parse("PT5M33S")) },
            { Assertions.assertThat(pace4.pacePerKm).isEqualTo(Duration.parse("PT5M23S")) },
        )
    }

    @Test
    fun `1km 페이스 리스트의 평균 페이스를 계산한다`() {
        // given
        val paces =
            listOf(
                Pace(1000.0, Duration.parse("PT7M30S")),
                Pace(1000.0, Duration.parse("PT7M40S")),
            )

        // when
        // then
        Assertions.assertThat(paces.averagePace().pacePerKm).isEqualTo(Duration.parse("PT7M35S"))
    }

    @Test
    fun `랜덤 페이스 리스트의 평균 페이스를 계산한다`() {
        // given
        val paces =
            listOf(
                Pace(800.0, Duration.ofSeconds(180)),
                Pace(1000.0, Duration.ofSeconds(300)),
                Pace(1200.0, Duration.ofSeconds(360)),
            )

        // when
        // then
        Assertions.assertThat(paces.averagePace().pacePerKm).isEqualTo(Duration.parse("PT4M35S"))
    }
}
