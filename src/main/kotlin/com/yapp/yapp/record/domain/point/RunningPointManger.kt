package com.yapp.yapp.record.domain.point

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RunningMetricsCalculator
import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class RunningPointManger(
    private val runningPointDao: RunningPointDao,
) {
    fun saveRunningPoint(
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

    fun saveNewRunningPoint(
        runningRecord: RunningRecord,
        lat: Double,
        lon: Double,
        heartRate: Int? = null,
        timeStamp: OffsetDateTime,
        totalRunningTimeMills: Long,
    ): RunningPoint {
        val preRunningPoint = runningPointDao.getPrePointByRunningRecord(runningRecord)
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = lat,
                lon = lon,
                orderNo = preRunningPoint.orderNo + 1,
                heartRate = heartRate,
                timeStamp = timeStamp,
                totalRunningTime = totalRunningTimeMills,
            )
        newRunningPoint.distance = RunningMetricsCalculator.calculateDistance(preRunningPoint, newRunningPoint)
        newRunningPoint.speedKmh = RunningMetricsCalculator.calculateSpeedKmh(preRunningPoint, newRunningPoint)
        newRunningPoint.totalRunningDistance = preRunningPoint.totalRunningDistance + newRunningPoint.distance
        newRunningPoint.pace = Pace(distanceMeter = newRunningPoint.totalRunningDistance, durationMills = totalRunningTimeMills)
        return runningPointDao.save(newRunningPoint)
    }

    fun getRunningPoints(runningRecord: RunningRecord): List<RunningPoint> {
        return runningPointDao.getAllPointByRunningRecord(runningRecord)
    }
}
