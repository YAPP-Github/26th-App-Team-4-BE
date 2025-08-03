package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface DeletedUserRepository : JpaRepository<DeletedUser, Long> {
    @Query("SELECT u.id FROM DeletedUser u where u.deletedAt <= :at")
    fun findIdsByDeletedAtBefore(at: OffsetDateTime): List<Long>
}
