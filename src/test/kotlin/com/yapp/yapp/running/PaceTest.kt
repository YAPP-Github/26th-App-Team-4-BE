package com.yapp.yapp.running

import com.yapp.yapp.support.BaseEntityTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import java.time.Duration

class PaceTest : BaseEntityTest() {
    @Test
    fun `페이스를 계산한다`() {
        // given
        val pace1 = Pace(1.0, Duration.parse("PT7M10S"))
        val pace2 = Pace(2.0, Duration.parse("PT7M10S"))
        val pace3 = Pace(3.0, Duration.parse("PT7M10S"))
        val pace4 = Pace(3.1, Duration.parse("PT7M10S"))

        // when
        // then
        assertAll(
            { Assertions.assertThat(pace1.pacePerKm).isEqualTo(Duration.parse("PT7M10S")) },
            { Assertions.assertThat(pace2.pacePerKm).isEqualTo(Duration.parse("PT3M35S")) },
            { Assertions.assertThat(pace3.pacePerKm).isEqualTo(Duration.parse("PT2M23S")) },
            { Assertions.assertThat(pace4.pacePerKm).isEqualTo(Duration.parse("PT2M19S")) },
        )
    }
}
