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
        val user = userDao.findByEmail(email)
        val userInfo = user?.toUserInfo() ?: save(email, name, profileImage)
        return userInfo
    }

    fun User.toUserInfo(): UserInfo {
        return UserInfo(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImage = this.profileImage,
        )
    }

    fun save(
        email: String,
        name: String?,
        profileImage: String?,
    ): UserInfo {
        val tempName = name ?: UsernameGenerator.generate(email)
        val userProfile = profileImage ?: defaultProfileImage
        val user = userDao.save(email, tempName, userProfile)
        return UserInfo(user.id, user.email, user.name, user.profileImage, true)
    }

    fun getActiveUser(id: Long): User {
        return userDao.getByIdAndIsDeletedFalse(id)
    }
}
