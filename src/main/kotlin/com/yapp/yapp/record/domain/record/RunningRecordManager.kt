package com.yapp.yapp.record.domain.record

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RecordStatus
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.point.RunningPointDao
import com.yapp.yapp.user.domain.User
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
        user: User,
    ): RunningRecord {
        val runningRecord = runningRecordDao.getById(id)
        if (!runningRecord.isOwnedBy(user)) {
            throw CustomException(ErrorCode.RECORD_NO_MATCHED)
        }
        return runningRecord
    }

    fun getRunningRecords(
        user: User,
        searchType: RecordsSearchType,
        targetDate: OffsetDateTime,
        pageable: Pageable,
    ): List<RunningRecord> {
        return runningRecordDao.getRunningRecordList(
            user = user,
            targetDate = targetDate,
            type = searchType,
            pageable = pageable,
        )
    }

    fun findRecentRunningRecord(user: User): RunningRecord? {
        return runningRecordDao.findRecentRunningRecord(user)
    }

    fun getTotalRecord(
        user: User,
        searchType: RecordsSearchType,
        targetDate: OffsetDateTime,
    ): RunningRecord {
        val runningRecordList = runningRecordDao.getRunningRecordList(user = user, targetDate = targetDate, type = searchType)
        val totalRunningRecord = RunningRecord(user = user, recordStatus = RecordStatus.DONE)
        if (runningRecordList.isEmpty()) {
            return totalRunningRecord
        }
        totalRunningRecord.update(
            totalDistance = runningRecordList.sumOf { it.totalDistance },
            totalTime = runningRecordList.sumOf { it.totalTime },
            totalCalories = runningRecordList.sumOf { it.totalCalories },
            averageSpeed = if (runningRecordList.isEmpty()) 0.0 else runningRecordList.map { it.averageSpeed }.average(),
            averagePace = runningRecordList.map { it.averagePace.toMills() }.average().let { Pace(it.toLong()) },
        )
        return totalRunningRecord
    }

    fun getThisWeekRunningCount(user: User): Int {
        return runningRecordDao.getThisWeekRunningCount(user)
    }

    fun start(
        user: User,
        startAt: OffsetDateTime,
    ): RunningRecord {
        val runningRecord = RunningRecord(user = user, startAt = startAt)
        runningRecord.start()
        return runningRecordDao.save(runningRecord)
    }

    fun stop(
        id: Long,
        user: User,
    ): RunningRecord {
        val runningRecord = getRunningRecord(id, user)
        runningRecord.pause()
        return runningRecord
    }

    fun finish(
        id: Long,
        user: User,
        runningPoints: List<RunningPoint>,
    ): RunningRecord {
        val record = getRunningRecord(id, user)
        record.finish()
        record.updateInfoByRunningPoints(runningPoints)
        return record
    }

    fun updateRecord(runningRecord: RunningRecord) {
        val runningPoints = runningPointDao.getAllPointByRunningRecord(runningRecord)
        runningRecord.updateInfoByRunningPoints(runningPoints)
    }
}
