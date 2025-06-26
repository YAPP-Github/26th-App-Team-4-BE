package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.RecordsSearchType
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.OffsetDateTime

@Component
class RunningRecordDao(
    private val runningRecordRepository: RunningRecordRepository,
) {
    fun save(runningRecord: RunningRecord): RunningRecord {
        return runningRecordRepository.save(runningRecord)
    }

    fun getById(id: Long): RunningRecord {
        return runningRecordRepository.findById(id)
            .orElseThrow { throw CustomException(ErrorCode.RECORD_NOT_FOUND) }
    }

    fun getRunningRecord(
        userId: Long,
        targetDate: OffsetDateTime,
        type: RecordsSearchType,
        pageable: Pageable,
    ): List<RunningRecord> {
        val startDate = getStartDate(targetDate, type)
        val endDate = getEndDate(targetDate, type)
        return runningRecordRepository.findByUserIdAndStartAtBetweenOrderByStartAtDesc(userId, startDate, endDate, pageable)
    }

    private fun getStartDate(
        targetDate: OffsetDateTime,
        type: RecordsSearchType,
    ): OffsetDateTime =
        when (type) {
            RecordsSearchType.WEEKLY -> targetDate.with(DayOfWeek.MONDAY)
            RecordsSearchType.MONTHLY -> targetDate.withDayOfMonth(1)
            RecordsSearchType.DAILY -> TimeProvider.from(targetDate.toLocalDate().atStartOfDay())
            RecordsSearchType.YEARLY -> targetDate.withDayOfYear(1)
            RecordsSearchType.ALL -> targetDate.minusYears(20L)
        }

    private fun getEndDate(
        targetDate: OffsetDateTime,
        type: RecordsSearchType,
    ): OffsetDateTime =
        when (type) {
            RecordsSearchType.WEEKLY -> targetDate.with(DayOfWeek.SUNDAY)
            RecordsSearchType.MONTHLY -> targetDate.withDayOfMonth(targetDate.toLocalDate().lengthOfMonth())
            RecordsSearchType.DAILY -> targetDate.withHour(0).plusDays(1).minusSeconds(1)
            RecordsSearchType.YEARLY -> targetDate.withDayOfYear(targetDate.toLocalDate().lengthOfYear())
            RecordsSearchType.ALL -> targetDate.plusYears(20L)
        }
}
