package com.yapp.yapp.running.domain.record

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.time.OffsetDateTime

interface RunningRecordRepository : CrudRepository<RunningRecord, Long> {
    fun findByUserIdAndStartAtBetweenOrderByStartAtDesc(
        userId: Long,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
        pageable: Pageable,
    ): List<RunningRecord>
}
