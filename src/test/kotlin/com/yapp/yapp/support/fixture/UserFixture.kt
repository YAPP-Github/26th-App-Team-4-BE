package com.yapp.yapp.support.fixture

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFixture(
    private val userRepository: UserRepository,
) {
    fun create(
        nickname: String = "test nickname",
        email: String = "test email",
        provider: ProviderType = ProviderType.APPLE,
        runnerType: RunnerType = RunnerType.BEGINNER,
    ): User =
        userRepository.save(
            User(
                nickname = nickname,
                email = email,
                provider = provider,
                runnerType = runnerType,
            ),
        )
}
