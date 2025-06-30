package com.yapp.yapp.support.fixture

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFixture(
    private val userRepository: UserRepository,
) {
    fun create(
        name: String = "test name",
        email: String = "test email",
        profileImage: String = "test profile",
        provider: ProviderType = ProviderType.APPLE,
    ): User =
        userRepository.save(
            User(
                name = name,
                email = email,
                profileImage = profileImage,
                provider = provider,
            ),
        )
}
