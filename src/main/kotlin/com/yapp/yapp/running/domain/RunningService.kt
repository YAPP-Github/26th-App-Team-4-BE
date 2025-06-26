package com.yapp.yapp.running.domain

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
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
class RunningService(
    private val runningRecordManager: RunningRecordManager,
    private val runningPointManger: RunningPointManger,
) {
    @Transactional
    fun start(
        userId: Long,
        request: RunningStartRequest,
    ): RunningStartResponse {
        val startAt = TimeProvider.parse(request.timeStamp)
        val runningRecord = runningRecordManager.start(userId, startAt)
        runningPointManger.saveRunningPoints(runningRecord, request.lat, request.lon, startAt)
        return RunningStartResponse(runningRecord.id)
    }

    @Transactional
    fun update(
        userId: Long,
        recordId: Long,
        request: RunningUpdateRequest,
    ): RunningUpdateResponse {
        val runningRecord = runningRecordManager.getRunningRecord(recordId, userId = userId)
        val newRunningPoint =
            runningPointManger.saveNewRunningPoints(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                heartRate = request.heartRate,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTime = Duration.parse(request.totalRunningTime),
            )
        return RunningUpdateResponse(newRunningPoint)
    }

    @Transactional
    fun stop(
        userId: Long,
        recordId: Long,
        request: RunningStopRequest,
    ): RunningStopResponse {
        val runningRecord = runningRecordManager.stop(recordId, userId = userId)
        return RunningStopResponse(userId, runningRecord.id)
    }

    @Transactional
    fun resume(
        userId: Long,
        recordId: Long,
        request: RunningResumeRequest,
    ): RunningResumeResponse {
        val runningRecord = runningRecordManager.resume(recordId, userId = userId)
        val newRunningPoints =
            runningPointManger.saveNewRunningPoints(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                heartRate = request.heartRate,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTime = Duration.parse(request.totalRunningTime),
            )
        return RunningResumeResponse(newRunningPoints)
    }

    @Transactional
    fun done(
        userId: Long,
        recordId: Long,
        request: RunningDoneRequest,
    ): RunningDoneResponse {
        val runningRecord = runningRecordManager.getRunningRecord(recordId, userId = userId)
        val runningPoints = runningPointManger.getRunningPoints(runningRecord)
        val finishRunningRecord = runningRecordManager.finish(recordId, userId, runningPoints)
        return RunningDoneResponse(finishRunningRecord)
    }
}
