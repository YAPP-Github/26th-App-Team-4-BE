package com.yapp.yapp.user

import com.yapp.yapp.user.domain.RunnerType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RunnerTypeTest {
    @Test
    fun `초보 러너 타입을 계산한다`() {
        // given
        val yseNoCount = 9

        // then
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 7, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.BEGINNER)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 8, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.BEGINNER)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 9, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.BEGINNER)
    }

    @Test
    fun `중급 러너 타입을 계산한다`() {
        // given
        val yseNoCount = 9

        // then
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 4, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.INTERMEDIATE)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 5, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.INTERMEDIATE)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 6, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.INTERMEDIATE)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 3, yseNoCount = 6))
            .isEqualTo(RunnerType.INTERMEDIATE)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 0, yseNoCount = 0))
            .isEqualTo(RunnerType.INTERMEDIATE)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 1, yseNoCount = 2))
            .isEqualTo(RunnerType.INTERMEDIATE)
    }

    @Test
    fun `고급 러너 타입을 계산한다`() {
        // given
        val yseNoCount = 9

        // then
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 0, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.EXPERT)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 1, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.EXPERT)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 2, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.EXPERT)
        Assertions.assertThat(RunnerType.calculateRunnerType(noCount = 3, yseNoCount = yseNoCount))
            .isEqualTo(RunnerType.EXPERT)
    }
}
