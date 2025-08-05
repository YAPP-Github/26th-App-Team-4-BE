package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): User?

    fun findByEmailAndIsDeletedFalse(email: String): User?

    fun existsByNickname(nickname: String): Boolean

    fun deleteByIdIn(ids: List<Long>)
}
