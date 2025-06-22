package com.yapp.yapp.user.domain

import com.yapp.yapp.user.api.request.UserCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userManager: UserManager,
) {
    @Transactional
    fun create(request: UserCreateRequest) =
        userManager.save(
            email = request.email,
            name = request.name,
            profile = request.profile,
        )

    @Transactional(readOnly = true)
    fun getUserById(id: Long): User {
        return userManager.getActiveUser(id)
    }

    @Transactional
    fun update(user: User) {
        val findUser = getUserById(user.id)
        findUser.name = user.name
        findUser.email = user.email
        findUser.profile = user.profile
    }

    @Transactional
    fun delete(user: User) {
        val findUser = getUserById(user.id)
        findUser.isDeleted = true
    }
}
