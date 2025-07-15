package com.yapp.yapp.user

import com.yapp.yapp.user.domain.RunnerType
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class RunnerTypeTest {
    @ParameterizedTest
    @ValueSource(ints = [7, 8, 9])
    fun `초보 러너 타입을 계산한다`(noCount: Int) {
        // given
        val yesNoCount = 9

        // then
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = noCount, yesNoCount = yesNoCount))
            .isEqualTo(RunnerType.BEGINNER)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = noCount, yesNoCount = yesNoCount))
            .isEqualTo(RunnerType.BEGINNER)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = noCount, yesNoCount = yesNoCount))
            .isEqualTo(RunnerType.BEGINNER)
    }

    @ParameterizedTest
    @CsvSource(
        "4, 9",
        "5, 9",
        "6, 9",
        "3, 6",
        "0, 0",
        "1, 2",
    )
    fun `중급 러너 타입을 계산한다`(
        noCount: Int,
        yesNoCount: Int,
    ) {
        // when & then
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = noCount, yesNoCount = yesNoCount))
            .isEqualTo(RunnerType.INTERMEDIATE)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3])
    fun `고급 러너 타입을 계산한다`(noCount: Int) {
        // given
        val yesNoCount = 9

        // then
        Assertions.assertThat(
            RunnerType.calculateRunnerType(noCount = noCount, yesNoCount = yesNoCount),
        )
            .isEqualTo(RunnerType.EXPERT)
    }
}
