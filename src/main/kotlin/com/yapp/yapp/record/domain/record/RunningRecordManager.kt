package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.point.RunningPointDao
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RunningRecordManager(
    private val runningRecordDao: RunningRecordDao,
    private val runningPointDao: RunningPointDao,
) {
    fun getRunningRecord(
        id: Long,
        userId: Long,
    ): RunningRecord {
        val runningRecord = runningRecordDao.getById(id)
        if (runningRecord.userId != userId) {
            throw CustomException(ErrorCode.RECORD_NO_MATCHED)
        }
        return runningRecord
    }

    fun getRunningRecords(
        userId: Long,
        type: RecordsSearchType,
        targetDate: OffsetDateTime,
        pageable: Pageable,
    ): List<RunningRecord> {
        return runningRecordDao.getRunningRecord(
            userId = userId,
            targetDate = targetDate,
            type = type,
            pageable = pageable,
        )
    }

    fun start(
        userId: Long,
        startAt: OffsetDateTime,
    ): RunningRecord {
        val runningRecord = RunningRecord(userId = userId, startAt = startAt)
        runningRecord.start()
        return runningRecordDao.save(runningRecord)
    }

    fun stop(
        id: Long,
        userId: Long,
    ): RunningRecord {
        val runningRecord = getRunningRecord(id, userId)
        runningRecord.pause()
        return runningRecord
    }

    fun finish(
        id: Long,
        userId: Long,
        runningPoints: List<RunningPoint>,
    ): RunningRecord {
        val record = getRunningRecord(id, userId)
        record.finish()
        record.updateInfoByRunningPoints(runningPoints)
        return record
    }

    fun updateRecord(runningRecord: RunningRecord) {
        val runningPoints = runningPointDao.getAllPointByRunningRecord(runningRecord)
        runningRecord.updateInfoByRunningPoints(runningPoints)
    }
}
