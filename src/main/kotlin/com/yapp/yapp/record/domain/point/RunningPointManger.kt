package com.yapp.yapp.record.domain.point

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.running.domain.RunningMetricsCalculator
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.OffsetDateTime

@Component
class RunningPointManger(
    private val runningPointDao: RunningPointDao,
) {
    fun saveRunningPoints(
        runningRecord: RunningRecord,
        lat: Double,
        lon: Double,
        timeStamp: OffsetDateTime,
    ) {
        val runningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = lat,
                lon = lon,
                timeStamp = timeStamp,
            )
        runningPointDao.save(runningPoint)
    }

    fun saveNewRunningPoints(
        runningRecord: RunningRecord,
        lat: Double,
        lon: Double,
        heartRate: Int?,
        timeStamp: OffsetDateTime,
        totalRunningTime: Duration,
    ): RunningPoint {
        val preRunningPoint = runningPointDao.getPrePointByRunningRecord(runningRecord)
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = lat,
                lon = lon,
                ord = preRunningPoint.ord + 1,
                heartRate = heartRate,
                timeStamp = timeStamp,
                totalRunningTime = totalRunningTime,
            )
        newRunningPoint.distance = RunningMetricsCalculator.calculateDistance(preRunningPoint, newRunningPoint)
        newRunningPoint.speed = RunningMetricsCalculator.calculateSpeed(preRunningPoint, newRunningPoint)
        newRunningPoint.totalRunningDistance = preRunningPoint.totalRunningDistance + newRunningPoint.distance
        newRunningPoint.pace = Pace(distance = newRunningPoint.totalRunningDistance, duration = totalRunningTime)
        return runningPointDao.save(newRunningPoint)
    }

    fun getRunningPoints(runningRecord: RunningRecord): List<RunningPoint> {
        return runningPointDao.getAllPointByRunningRecord(runningRecord)
    }
}
