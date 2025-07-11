package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

class PaceTest {
    @Test
    fun `페이스를 계산한다`() {
        // given
        val pace1 = Pace(1.0, TimeProvider.toMills(second = 1))
        val pace2 = Pace(2.0, TimeProvider.toMills(second = 1))
        val pace3 = Pace(3.0, TimeProvider.toMills(second = 1))
        val pace4 = Pace(3.1, TimeProvider.toMills(second = 1))

        // when

        // then
        assertAll(
            { Assertions.assertThat(pace1.toString()).isEqualTo("16:40 /km") },
            { Assertions.assertThat(pace2.toString()).isEqualTo("8:20 /km") },
            { Assertions.assertThat(pace3.toString()).isEqualTo("5:33 /km") },
            { Assertions.assertThat(pace4.toString()).isEqualTo("5:22 /km") },
        )
    }
}
