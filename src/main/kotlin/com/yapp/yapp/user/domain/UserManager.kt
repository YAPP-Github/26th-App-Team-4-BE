package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userDao: UserDao,
) {
    fun getUserInfo(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val user = userDao.findByEmail(email)
        val userInfo = user?.toUserInfo() ?: save(email, provider)
        return userInfo
    }

    fun User.toUserInfo(): UserInfo {
        return UserInfo(
            id = this.id,
            email = this.email,
            nickname = this.nickname,
            provider = this.provider,
        )
    }

    fun save(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val nickname = UsernameGenerator.generate(email)
        val user = userDao.save(email, nickname, provider)
        return UserInfo(user.id, user.email, user.nickname, provider, true)
    }

    fun getActiveUser(id: Long): User {
        return userDao.getByIdAndIsDeletedFalse(id)
    }
}
