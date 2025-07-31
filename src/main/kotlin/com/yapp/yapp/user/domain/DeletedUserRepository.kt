package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface DeletedUserRepository : JpaRepository<DeletedUser, Long> {
    fun findIdsByDeletedAtBefore(at: OffsetDateTime): List<Long>
}
