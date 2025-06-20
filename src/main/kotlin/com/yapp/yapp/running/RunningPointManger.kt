package com.yapp.yapp.running

import org.springframework.stereotype.Component
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
}
