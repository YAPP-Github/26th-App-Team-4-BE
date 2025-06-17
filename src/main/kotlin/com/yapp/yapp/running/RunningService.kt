package com.yapp.yapp.running

import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
import org.springframework.stereotype.Service

@Service
class RunningService(
    private val runningRecordDao: RunningRecordDao,
    private val runningPointDao: RunningPointDao,
) {
    fun start(request: RunningStartRequest): RunningStartResponse {
        // TODO 유저 정보 조회
        val runningRecord = runningRecordDao.save(RunningRecord())
        val runningPoint =
            runningPointDao.save(
                RunningPoint(
                    runningRecord = runningRecord,
                    lat = request.lat,
                    lon = request.lon,
                    timeStamp = request.timeStamp,
                ),
            )
        return RunningStartResponse(runningRecord.id)
    }

    fun update(request: RunningUpdateRequest): RunningUpdateResponse {
        val runningRecord = runningRecordDao.getById(request.recordId)
        val preRunningPoint = runningPointDao.getPrePointByRecordRecord(runningRecord)
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                heartRate = request.heartRate,
            )
        newRunningPoint.distance = RunningMetricsCalculator.calculateDistance(preRunningPoint, newRunningPoint)
        newRunningPoint.speed = RunningMetricsCalculator.calculateSpeed(preRunningPoint, newRunningPoint)
        val saveRunningPoint = runningPointDao.save(newRunningPoint)
        return RunningUpdateResponse(saveRunningPoint)
    }
}
