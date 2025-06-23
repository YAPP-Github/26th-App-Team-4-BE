package com.yapp.yapp.user.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class UserDao(
    private val userRepository: UserRepository,
) {
    fun save(
        email: String,
        name: String,
        profileImage: String,
    ): User {
        return userRepository.save(User(email = email, name = name, profileImage = profileImage))
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun getByIdAndIsDeletedFalse(id: Long): User {
        return userRepository.findByIdAndIsDeletedFalse(id)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
    }
}
