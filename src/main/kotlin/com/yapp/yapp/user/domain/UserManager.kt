package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userDao: UserDao,
    private val deletedUserDao: DeletedUserDao,
) {
    fun save(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val nickname = getRandomNickname(email)
        val user = userDao.save(email, nickname, provider)
        return UserInfo(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            provider = user.provider,
            runnerType = user.runnerType,
            isNew = true,
        )
    }

    fun getUserInfo(
        email: String,
        provider: ProviderType,
    ): UserInfo {
        val user = userDao.findByEmail(email) ?: return save(email, provider)
        if (user.isDeleted) {
            user.isDeleted = false
        }
        return UserInfo(user)
    }

    fun delete(
        userId: Long,
        reason: String?,
    ) {
        val user = userDao.getByIdAndIsDeletedFalse(userId)
        val deletedUser = DeletedUser(id = user.id, reason = reason)
        user.isDeleted = true
        deletedUserDao.save(deletedUser)
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
