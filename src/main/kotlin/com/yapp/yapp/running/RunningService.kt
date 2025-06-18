package com.yapp.yapp.running

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.running.Pace.Companion.averagePace
import com.yapp.yapp.running.RunningMetricsCalculator.roundTo
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningResumeRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningStopRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningResumeResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningStopResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
@Transactional
class RunningService(
    private val runningRecordDao: RunningRecordDao,
    private val runningPointDao: RunningPointDao,
) {
    fun start(request: RunningStartRequest): RunningStartResponse {
        // TODO 유저 정보 조회
        val runningRecord = runningRecordDao.save(RunningRecord(startAt = TimeProvider.parse(request.timeStamp)))
        runningRecord.startRunning()
        val runningPoint =
            runningPointDao.save(
                RunningPoint(
                    runningRecord = runningRecord,
                    lat = request.lat,
                    lon = request.lon,
                    timeStamp = TimeProvider.parse(request.timeStamp),
                ),
            )
        return RunningStartResponse(runningRecord.id)
    }

    fun update(request: RunningUpdateRequest): RunningUpdateResponse {
        val runningRecord = runningRecordDao.getById(request.recordId)
        val preRunningPoint = runningPointDao.getPrePointByRunningRecord(runningRecord)
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                ord = preRunningPoint.ord + 1,
                heartRate = request.heartRate,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTime = Duration.parse(request.totalRunningTime),
            )
        newRunningPoint.distance = RunningMetricsCalculator.calculateDistance(preRunningPoint, newRunningPoint)
        newRunningPoint.speed = RunningMetricsCalculator.calculateSpeed(preRunningPoint, newRunningPoint)
        newRunningPoint.totalRunningDistance = preRunningPoint.totalRunningDistance + newRunningPoint.distance
        newRunningPoint.pace = Pace(distance = newRunningPoint.totalRunningDistance, duration = Duration.parse(request.totalRunningTime))
        val saveRunningPoint = runningPointDao.save(newRunningPoint)
        return RunningUpdateResponse(saveRunningPoint)
    }

    fun stop(request: RunningStopRequest): RunningStopResponse {
        val runningRecord = runningRecordDao.getById(request.recordId)
        runningRecord.stopRunning()
        return RunningStopResponse(runningRecord.id, request.recordId)
    }

    fun resume(request: RunningResumeRequest): RunningResumeResponse {
        val runningRecord = runningRecordDao.getById(request.recordId)
        val preRunningPoint = runningPointDao.getPrePointByRunningRecord(runningRecord)
        val newRunningPoint =
            RunningPoint(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                ord = preRunningPoint.ord + 1,
                heartRate = request.heartRate,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTime = Duration.parse(request.totalRunningTime),
            )
        runningRecord.resumeRunning()
        val saveRunningPoint = runningPointDao.save(newRunningPoint)
        return RunningResumeResponse(saveRunningPoint)
    }

    fun done(request: RunningDoneRequest): RunningDoneResponse {
        val runningRecord = runningRecordDao.getById(request.recordId)
        runningRecord.finishRunning()
        val runningPoints = runningPointDao.getAllPointByRunningRecord(runningRecord)
        runningRecord.totalRunningTime = runningPoints.last().totalRunningTime
        runningRecord.totalRunningDistance = runningPoints.last().totalRunningDistance
        runningRecord.totalCalories = runningPoints.sumOf { it.calories }
        runningRecord.averageSpeed = (runningPoints.sumOf { it.speed } / runningPoints.size).roundTo()
        runningRecord.averagePace = runningPoints.map { it.pace }.averagePace()
        return RunningDoneResponse(runningRecord)
    }

    fun getRunningRecord(runningId: Long) {
        val runningRecord = runningRecordDao.getById(runningId)
    }
}
