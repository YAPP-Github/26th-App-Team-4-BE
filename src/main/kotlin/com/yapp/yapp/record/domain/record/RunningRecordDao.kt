package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.user.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
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

    fun getRunningRecordList(
        userId: Long,
        targetDate: OffsetDateTime,
        type: RecordsSearchType,
        pageable: Pageable? = null,
    ): List<RunningRecord> {
        val startDate = type.getStartDate(targetDate)
        val endDate = type.getEndDate(targetDate)
        return runningRecordRepository.findByUserIdAndStartAtBetweenOrderByStartAtDesc(
            userId,
            startDate,
            endDate,
            pageable,
        )
    }

    fun findRecentRunningRecord(user: User): RunningRecord? {
        return runningRecordRepository.findFirstByUserIdOrderByStartAtDesc(user.id)
    }

    fun getThisWeekRunningCount(user: User): Int {
        val targetDate = TimeProvider.now()
        val startDate = RecordsSearchType.WEEKLY.getStartDate(targetDate)
        val endDate = RecordsSearchType.WEEKLY.getEndDate(targetDate)
        return runningRecordRepository.countByUserIdAndStartAtBetween(userId = user.id, startDate = startDate, endDate = endDate)
    }
}
