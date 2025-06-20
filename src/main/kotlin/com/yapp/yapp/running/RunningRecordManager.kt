package com.yapp.yapp.running

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RunningRecordManager(
    private val runningRecordDao: RunningRecordDao,
) {
    fun startRunningRecord(startAt: OffsetDateTime): RunningRecord {
        val runningRecord = RunningRecord(startAt = startAt)
        runningRecord.startRunning()
        return runningRecordDao.save(runningRecord)
    }

    fun getRunningRecord(id: Long): RunningRecord = runningRecordDao.getById(id)

    fun stopRunningRecord(id: Long): RunningRecord {
        val runningRecord = runningRecordDao.getById(id)
        runningRecord.finishRunning()
        return runningRecord
    }

    fun resumeRunningRecord(id: Long): RunningRecord {
        val runningRecord = runningRecordDao.getById(id)
        runningRecord.resumeRunning()
        return runningRecord
    }
}
