package com.yapp.yapp.record.domain.record

import com.yapp.yapp.user.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.OffsetDateTime

interface RunningRecordRepository : CrudRepository<RunningRecord, Long> {
    fun findByUserAndStartAtBetweenOrderByStartAtDesc(
        user: User,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
        pageable: Pageable?,
    ): List<RunningRecord>

    fun findFirstByUserOrderByStartAtDesc(user: User): RunningRecord?

    fun countByUserAndStartAtBetween(
        user: User,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
    ): Int

    @Query("SELECT r.id from RunningRecord r WHERE r.user.id IN :userIds")
    fun findIdsByUserIdIn(userIds: List<Long>): List<Long>

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM RunningRecord r WHERE r.user.id IN :userIds")
    fun deleteByUserIdIn(userIds: List<Long>)
}
