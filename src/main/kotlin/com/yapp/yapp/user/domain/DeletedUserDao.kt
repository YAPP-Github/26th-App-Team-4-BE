package com.yapp.yapp.user.domain

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class DeletedUserDao(
    private val deletedUserRepository: DeletedUserRepository,
) {
    fun save(deletedUser: DeletedUser) {
        deletedUserRepository.save(deletedUser)
    }

    fun delete(userId: Long) {
        deletedUserRepository.deleteById(userId)
    }

    fun getDeletedUserIds(cutoffDateTime: OffsetDateTime): List<Long> {
        return deletedUserRepository.findUserIdsByDeletedAtBefore(cutoffDateTime)
    }

    fun deleteByUserIdIn(userIds: List<Long>) {
        deletedUserRepository.deleteByUserIdIn(userIds)
    }
}
