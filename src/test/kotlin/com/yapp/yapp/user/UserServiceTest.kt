package com.yapp.yapp.user

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.record.domain.record.RunningRecordDao
import com.yapp.yapp.support.BaseServiceTest
import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.UserDao
import com.yapp.yapp.user.domain.UserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.time.OffsetDateTime

class UserServiceTest : BaseServiceTest() {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var runningRecordDao: RunningRecordDao

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

    @Test
    fun `회원 탈퇴 후 한달 뒤 회원 삭제한다`() {
        // given
        val user = userFixture.create(email = "test@test.com")
        val deletedAt = TimeProvider.now().minusMonths(1)
        runningFixture.createRunningRecord(user = user, totalSeconds = 60 * 20)
        userFixture.createWithdrawUser(user = user, deletedAt = deletedAt)

        // when
        userService.cleanup()

        Assertions.assertThat(userDao.findByEmailAndIsDeletedFalse(user.email)).isNull()
        Assertions.assertThat(
            runningRecordDao.getRunningRecordList(
                user = user,
                targetDate = OffsetDateTime.now(),
                type = RecordsSearchType.ALL,
                pageable = PageRequest.of(1, 10),
            ),
        ).isEmpty()
    }

    @Test
    fun `회원 탈퇴 후 재가입한다`() {
        // given
        val user = userFixture.create(email = "test@test.com")
        val deletedAt = TimeProvider.now().minusMonths(1)
        userFixture.createWithdrawUser(user = user, deletedAt = deletedAt)
        runningFixture.createRunningRecord(user = user, totalSeconds = 60 * 20)

        // when
        userService.cleanup()

        Assertions.assertThat(userDao.findByEmailAndIsDeletedFalse(user.email)).isNull()
        Assertions.assertThat(
            runningRecordDao.getRunningRecordList(
                user = user,
                targetDate = OffsetDateTime.now(),
                type = RecordsSearchType.ALL,
                pageable = PageRequest.of(1, 10),
            ),
        ).isEmpty()
        Assertions.assertThat(userDao.save(user.email, user.nickname, user.provider)).isNotNull
    }
}
