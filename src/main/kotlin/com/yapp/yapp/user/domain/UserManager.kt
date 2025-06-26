package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
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
        provider: ProviderType,
    ): UserInfo {
        val user = userDao.findByEmailAndProvider(email, provider)
        val userInfo = user?.toUserInfo() ?: save(email, name, profileImage, provider)
        return userInfo
    }

    fun User.toUserInfo(): UserInfo {
        return UserInfo(
            id = this.id,
            email = this.email,
            name = this.name,
            provider = this.provider,
            profileImage = this.profileImage,
        )
    }

    fun save(
        email: String,
        name: String?,
        profileImage: String?,
        provider: ProviderType,
    ): UserInfo {
        userDao.findByEmail(email)?.let {
            throw CustomException(ErrorCode.USER_ALREADY_EXISTS)
        }
        val tempName = name ?: UsernameGenerator.generate(email)
        val userProfile = profileImage ?: defaultProfileImage
        val user = userDao.save(email, tempName, userProfile, provider)
        return UserInfo(user.id, user.email, user.name, user.profileImage, provider, true)
    }

    fun getActiveUser(id: Long): User {
        return userDao.getByIdAndIsDeletedFalse(id)
    }
}
