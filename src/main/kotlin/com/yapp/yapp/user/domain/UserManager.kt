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
        val nickname = getRandomNickname(email)
        val user = userDao.save(email, nickname, provider)
        return UserInfo(user.id, user.email, user.nickname, provider, true)
    }

    private fun getRandomNickname(email: String): String {
        var nickname: String
        do {
            nickname = NicknameGenerator.generate(email)
        } while (userDao.existsByNickname(nickname))
        return nickname
    }

    fun getActiveUser(id: Long): User {
        return userDao.getByIdAndIsDeletedFalse(id)
    }
}
