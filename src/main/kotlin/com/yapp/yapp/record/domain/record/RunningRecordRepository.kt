package com.yapp.yapp.record.domain.record

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.time.OffsetDateTime

interface RunningRecordRepository : CrudRepository<RunningRecord, Long> {
    fun findByUserIdAndStartAtBetweenOrderByStartAtDesc(
        userId: Long,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
        pageable: Pageable?,
    ): List<RunningRecord>

    fun findFirstByUserIdOrderByStartAtDesc(userId: Long): RunningRecord?

    fun countByUserIdAndStartAtBetween(
        userId: Long,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
    ): Int
}
