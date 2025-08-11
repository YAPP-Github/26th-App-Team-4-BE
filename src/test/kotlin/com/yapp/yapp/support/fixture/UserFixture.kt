package com.yapp.yapp.support.fixture

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.user.domain.DeletedUser
import com.yapp.yapp.user.domain.DeletedUserRepository
import com.yapp.yapp.user.domain.NicknameGenerator
import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class UserFixture(
    private val userRepository: UserRepository,
    private val userGoalFixture: UserGoalFixture,
    private val deletedRepository: DeletedUserRepository,
) {
    fun create(
        email: String = "test email",
        provider: ProviderType = ProviderType.APPLE,
        runnerType: RunnerType = RunnerType.BEGINNER,
    ): User =
        userRepository.save(
            User(
                nickname = NicknameGenerator.generate(email),
                email = email,
                provider = provider,
                runnerType = runnerType,
            ),
        )

    fun createWithGoal(
        email: String = "test email",
        provider: ProviderType = ProviderType.APPLE,
        runnerType: RunnerType = RunnerType.BEGINNER,
    ): User {
        val user =
            userRepository.save(
                User(
                    nickname = NicknameGenerator.generate(email),
                    email = email,
                    provider = provider,
                    runnerType = runnerType,
                ),
            )
        userGoalFixture.create(user)
        return user
    }

    fun createWithdrawUser(
        user: User,
        reason: String? = null,
        deletedAt: OffsetDateTime,
    ): DeletedUser {
        user.isDeleted = true
        val deletedUser = DeletedUser(reason = reason, deletedAt = deletedAt, user = user)
        return deletedRepository.save(deletedUser)
    }
}
