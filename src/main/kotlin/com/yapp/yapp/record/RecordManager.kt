package com.yapp.yapp.record

import com.yapp.yapp.running.Pace.Companion.averagePace
import com.yapp.yapp.running.RunningMetricsCalculator.roundTo
import com.yapp.yapp.running.RunningPoint
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RecordManager(
    private val recordDao: RecordDao,
) {
    fun startRecord(startAt: OffsetDateTime): Record {
        val record = Record(startAt = startAt)
        record.start()
        return recordDao.save(record)
    }

    fun getRecord(id: Long): Record = recordDao.getById(id)

    fun stopRecord(id: Long): Record {
        val record = recordDao.getById(id)
        record.finish()
        return record
    }

    fun resumeRecord(id: Long): Record {
        val record = recordDao.getById(id)
        record.resume()
        return record
    }

    fun finishRecord(
        id: Long,
        runningPoints: List<RunningPoint>,
    ): Record {
        val record = recordDao.getById(id)
        record.finish()
        record.totalTime = runningPoints.last().totalRunningTime
        record.totalDistance = runningPoints.last().totalRunningDistance
        record.totalCalories = runningPoints.sumOf { it.calories }
        record.averageSpeed = (runningPoints.sumOf { it.speed } / runningPoints.size).roundTo()
        record.averagePace = runningPoints.map { it.pace }.averagePace()
        return record
    }
}
