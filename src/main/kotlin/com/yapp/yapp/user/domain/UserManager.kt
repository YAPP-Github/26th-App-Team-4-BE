package com.yapp.yapp.user.domain

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userDao: UserDao,
    @Value("\${user.default.profile}")
    private val defaultProfileImage: String,
) {
    fun getUserInfo(
        email: String,
        name: String?,
        profileImage: String?,
    ): UserInfo {
        var isNew = false
        val user =
            userDao.findByEmail(email) ?: run {
                val username = name ?: UsernameGenerator.generate(email)
                val userProfile = profileImage ?: defaultProfileImage
                isNew = true
                userDao.save(email, username, userProfile)
            }
        return UserInfo(user.id, user.email, user.name, user.profileImage, isNew)
    }

    fun getActiveUser(id: Long): User {
        return userDao.getByIdAndIsDeletedFalse(id)
    }
}
