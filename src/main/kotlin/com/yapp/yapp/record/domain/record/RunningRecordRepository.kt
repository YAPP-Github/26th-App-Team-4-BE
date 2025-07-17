package com.yapp.yapp.record.domain.record

import com.yapp.yapp.user.domain.User
import org.springframework.data.domain.Pageable
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
}
