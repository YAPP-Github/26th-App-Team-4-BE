package com.yapp.yapp.user

import com.yapp.yapp.support.BaseServiceTest
import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.UserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest : BaseServiceTest() {
    @Autowired
    lateinit var userService: UserService

    @Test
    fun `러닝 기록이 없는 사용자의 추천 페이스는 기본 값이다`() {
        // given
        val runnerType = RunnerType.BEGINNER
        val user = userFixture.create(runnerType = runnerType)

        // when
        val recommendPace = userService.getRecommendPace(user.id)

        // then
        Assertions.assertThat(recommendPace).isEqualTo(runnerType.recommendPace)
    }

    @Test
    fun `러닝 기록이 있는 사용자의 추천 페이스를 계산한다`() {
        // given
        val runnerType = RunnerType.BEGINNER
        val user = userFixture.create(runnerType = runnerType)
        runningFixture.createRunningRecord(user = user, totalSeconds = 60 * 20)

        // when
        val recommendPace = userService.getRecommendPace(user.id)

        // then
        Assertions.assertThat(recommendPace.millsPerKm).isCloseTo(runnerType.recommendPace.millsPerKm, Assertions.within(1000L * 60))
    }
}
