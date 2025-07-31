package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class UserDao(
    private val userRepository: UserRepository,
) {
    fun save(
        email: String,
        nickname: String,
        provider: ProviderType,
    ): User {
        return userRepository.save(
            User(
                email = email,
                nickname = nickname,
                provider = provider,
            ),
        )
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun getByIdAndIsDeletedTrue(id: Long): User {
        return userRepository.findByIdAndIsDeletedTrue(id)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
    }

    fun getByIdAndIsDeletedFalse(id: Long): User {
        return userRepository.findByIdAndIsDeletedFalse(id)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
    }

    fun existsByNickname(nickname: String): Boolean {
        return userRepository.existsByNickname(nickname)
    }
}
