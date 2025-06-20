package com.yapp.yapp.running

import com.yapp.yapp.running.Pace.Companion.averagePace
import com.yapp.yapp.running.RunningMetricsCalculator.roundTo
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

    fun finishRunningRecord(
        id: Long,
        runningPoints: List<RunningPoint>,
    ): RunningRecord {
        val runningRecord = runningRecordDao.getById(id)
        runningRecord.finishRunning()
        runningRecord.totalRunningTime = runningPoints.last().totalRunningTime
        runningRecord.totalRunningDistance = runningPoints.last().totalRunningDistance
        runningRecord.totalCalories = runningPoints.sumOf { it.calories }
        runningRecord.averageSpeed = (runningPoints.sumOf { it.speed } / runningPoints.size).roundTo()
        runningRecord.averagePace = runningPoints.map { it.pace }.averagePace()
        return runningRecord
    }
}
