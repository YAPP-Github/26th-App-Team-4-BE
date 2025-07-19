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
        user: User,
        targetDate: OffsetDateTime,
        type: RecordsSearchType,
        pageable: Pageable? = null,
    ): List<RunningRecord> {
        val startDate = type.getStartDate(targetDate)
        val endDate = type.getEndDate(targetDate)
        return runningRecordRepository.findByUserAndStartAtBetweenOrderByStartAtDesc(
            user,
            startDate,
            endDate,
            pageable,
        )
    }

    fun findRecentRunningRecord(user: User): RunningRecord? {
        return runningRecordRepository.findFirstByUserOrderByStartAtDesc(user)
    }

    fun getThisWeekRunningCount(user: User): Int {
        val targetDate = TimeProvider.now()
        val startDate = RecordsSearchType.WEEKLY.getStartDate(targetDate)
        val endDate = RecordsSearchType.WEEKLY.getEndDate(targetDate)
        return runningRecordRepository.countByUserAndStartAtBetween(user = user, startDate = startDate, endDate = endDate)
    }
}
