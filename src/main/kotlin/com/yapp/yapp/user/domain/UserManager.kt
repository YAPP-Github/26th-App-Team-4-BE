package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userDao: UserDao,
) {
    fun save(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val nickname = getRandomNickname(email)
        val user = userDao.save(email, nickname, provider)
        return UserInfo(user.id, user.email, user.nickname, provider, true)
    }

    fun getUserInfo(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val user = userDao.findByEmail(email)
        if (user == null) {
            return save(email, provider)
        }
        return UserInfo(user)
    }

    fun getUserInfo(id: Long): UserInfo {
        val user = userDao.getByIdAndIsDeletedFalse(id)
        return UserInfo(user)
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

    fun updateRunnerType(
        user: User,
        runnerType: RunnerType,
    ) {
        user.updateRunnerType(runnerType)
    }
}
