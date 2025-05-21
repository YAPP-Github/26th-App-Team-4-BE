package com.yapp.yapp.user.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.user.api.request.UserCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(request: UserCreateRequest) = userRepository.save(User(email = request.email, name = request.name))

    @Transactional(readOnly = true)
    fun getUserById(id: Long): User {
        return userRepository.findByIdAndIsDeletedFalse(id)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun update(user: User) {
        val findUser = getUserById(user.id)
        findUser.name = user.name
        findUser.email = user.email
    }

    @Transactional
    fun delete(user: User) {
        val findUser = getUserById(user.id)
        findUser.isDeleted = true
    }
}
