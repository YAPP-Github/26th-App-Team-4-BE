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
@Transactional
class RunningService(
    private val runningRecordManager: RunningRecordManager,
    private val runningPointManger: RunningPointManger,
) {
    fun start(
        userId: Long,
        request: RunningStartRequest,
    ): RunningStartResponse {
        val startAt = TimeProvider.parse(request.timeStamp)
        val runningRecord = runningRecordManager.start(userId, startAt)
        runningPointManger.saveRunningPoints(runningRecord, request.lat, request.lon, startAt)
        return RunningStartResponse(runningRecord.id)
    }

    fun update(
        userId: Long,
        request: RunningUpdateRequest,
    ): RunningUpdateResponse {
        val runningRecord = runningRecordManager.getRunningRecord(userId, request.recordId)
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

    fun stop(
        userId: Long,
        request: RunningStopRequest,
    ): RunningStopResponse {
        val runningRecord = runningRecordManager.stop(request.recordId)
        return RunningStopResponse(userId, runningRecord.id)
    }

    fun resume(request: RunningResumeRequest): RunningResumeResponse {
        val runningRecord = runningRecordManager.resume(request.recordId)
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

    fun done(
        userId: Long,
        request: RunningDoneRequest,
    ): RunningDoneResponse {
        val runningRecord = runningRecordManager.getRunningRecord(userId, request.recordId)
        val runningPoints = runningPointManger.getRunningPoints(runningRecord)
        val finishRunningRecord = runningRecordManager.finish(request.recordId, runningPoints)
        return RunningDoneResponse(finishRunningRecord)
    }
}
