package com.yapp.yapp.user.domain

import org.springframework.stereotype.Component

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
}
