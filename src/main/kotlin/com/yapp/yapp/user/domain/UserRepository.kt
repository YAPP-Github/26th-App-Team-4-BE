package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): User?

    fun findByEmailAndIsDeletedFalse(email: String): User?

    fun existsByNickname(nickname: String): Boolean

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM User u WHERE u.id IN :ids")
    fun deleteByIdIn(ids: List<Long>)
}
