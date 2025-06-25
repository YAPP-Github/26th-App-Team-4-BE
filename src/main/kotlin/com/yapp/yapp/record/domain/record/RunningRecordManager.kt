package com.yapp.yapp.record.domain.record

import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.running.Pace.Companion.averagePace
import com.yapp.yapp.running.RunningMetricsCalculator.roundTo
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RunningRecordManager(
    private val runningRecordDao: RunningRecordDao,
) {
    fun startRecord(startAt: OffsetDateTime): RunningRecord {
        val runningRecord = RunningRecord(startAt = startAt)
        runningRecord.start()
        return runningRecordDao.save(runningRecord)
    }

    fun getRecord(id: Long): RunningRecord = runningRecordDao.getById(id)

    fun stopRecord(id: Long): RunningRecord {
        val record = runningRecordDao.getById(id)
        record.finish()
        return record
    }

    fun resumeRecord(id: Long): RunningRecord {
        val record = runningRecordDao.getById(id)
        record.resume()
        return record
    }

    fun finishRecord(
        id: Long,
        runningPoints: List<RunningPoint>,
    ): RunningRecord {
        val record = runningRecordDao.getById(id)
        record.finish()
        record.totalTime = runningPoints.last().totalRunningTime
        record.totalDistance = runningPoints.last().totalRunningDistance
        record.totalCalories = runningPoints.sumOf { it.calories }
        record.averageSpeed = (runningPoints.sumOf { it.speed } / runningPoints.size).roundTo()
        record.averagePace = runningPoints.map { it.pace }.averagePace()
        return record
    }
}
