package com.yapp.yapp.user.domain

import com.yapp.yapp.user.api.response.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userManager: UserManager,
) {
    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userManager.getActiveUser(id)
        return UserResponse(user.id, user.name, user.email, user.profileImage, user.provider)
    }

    @Transactional
    fun delete(id: Long) {
        val findUser = userManager.getActiveUser(id)
        findUser.isDeleted = true
    }
}
