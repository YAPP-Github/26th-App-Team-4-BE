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
        timeStamp: OffsetDateTime,
        totalRunningTimeMills: Long,
    ): RunningPoint {
        val preRunningPoint = getRecentRunningPoint(runningRecord)
        val newRunningPoint =
            createRunningPoint(
                preRunningPoint = preRunningPoint,
                runningRecord = runningRecord,
                lat = lat,
                lon = lon,
                timeStamp = timeStamp,
                totalRunningTimeMills = totalRunningTimeMills,
            )
        return runningPointDao.save(newRunningPoint)
    }

    fun saveAllRunningPoints(
        runningRecord: RunningRecord,
        runningPoints: List<RunningPoint>,
    ) {
        runningPointDao.saveAll(runningRecord = runningRecord, runningPoints = runningPoints)
    }

    fun createRunningPoint(
        preRunningPoint: RunningPoint,
        runningRecord: RunningRecord,
        lat: Double,
        lon: Double,
        timeStamp: OffsetDateTime,
        totalRunningTimeMills: Long,
    ): RunningPoint {
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = lat,
                lon = lon,
                orderNo = preRunningPoint.orderNo + 1,
                timeStamp = timeStamp,
                totalRunningTime = totalRunningTimeMills,
            )
        newRunningPoint.distance = RunningMetricsCalculator.calculateDistance(preRunningPoint, newRunningPoint)
        newRunningPoint.totalRunningDistance = preRunningPoint.totalRunningDistance + newRunningPoint.distance
        newRunningPoint.pace = Pace(distanceMeter = newRunningPoint.totalRunningDistance, durationMills = totalRunningTimeMills)
        return newRunningPoint
    }

    fun getRunningPoints(runningRecord: RunningRecord): List<RunningPoint> {
        return runningPointDao.getAllPointByRunningRecord(runningRecord)
    }

    fun getRecentRunningPoint(runningRecord: RunningRecord): RunningPoint {
        return runningPointDao.getPrePointByRunningRecord(runningRecord)
    }
}
