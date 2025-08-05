package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface DeletedUserRepository : JpaRepository<DeletedUser, Long> {
    @Query("SELECT d.user.id FROM DeletedUser d where d.deletedAt <= :at")
    fun findUserIdsByDeletedAtBefore(at: OffsetDateTime): List<Long>

    @Modifying
    @Query("DELETE FROM DeletedUser d WHERE d.user.id in :userIds")
    fun deleteByUserIdIn(userIds: List<Long>)
}
