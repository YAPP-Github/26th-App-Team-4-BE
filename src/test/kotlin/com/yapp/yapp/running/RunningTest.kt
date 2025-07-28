package com.yapp.yapp.running

import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.support.BaseSupportMethod
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import java.time.OffsetDateTime
import java.util.UUID

@SpringBootTest
class RunningTest : BaseSupportMethod() {
    @ParameterizedTest
    @CsvSource(
        "2025-07-27T23:59+09:00, 07월 27일 저녁 러닝",
        "2025-07-28T00:00+09:00, 07월 28일 새벽 러닝",
        "2025-07-28T04:59+09:00, 07월 28일 새벽 러닝",
        "2025-07-01T05:00+09:00, 07월 01일 아침 러닝",
        "2025-07-28T11:59+09:00, 07월 28일 아침 러닝",
        "2025-07-28T12:00+09:00, 07월 28일 오후 러닝",
        "2025-07-28T17:59+09:00, 07월 28일 오후 러닝",
        "2025-07-28T18:00+09:00, 07월 28일 저녁 러닝",
    )
    fun `러닝 제목 생성 테스트`(
        startAtString: String,
        expectedTitle: String,
    ) {
        // given
        val user = userFixture.create(email = "test${UUID.randomUUID()}@test.com")
        val startAt = OffsetDateTime.parse(startAtString)
        val record =
            RunningRecord(
                user = user,
                startAt = startAt,
            )

        // when
        val actualTitle = record.title

        // then
        Assertions.assertThat(actualTitle).isEqualTo(expectedTitle)
    }
}
