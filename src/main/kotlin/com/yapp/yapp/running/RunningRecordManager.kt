package com.yapp.yapp.running

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RunningRecordManager(
    private val runningRecordDao: RunningRecordDao,
) {
    fun start(startAt: OffsetDateTime): RunningRecord {
        val runningRecord = RunningRecord(startAt = startAt)
        runningRecord.startRunning()
        return runningRecordDao.save(runningRecord)
    }

    fun getRunningRecord(id: Long): RunningRecord = runningRecordDao.getById(id)
}
