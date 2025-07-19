package com.yapp.yapp.running.domain

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.api.response.RunningPointResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningPauseResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.user.domain.UserManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RunningService(
    private val runningRecordManager: RunningRecordManager,
    private val runningPointManger: RunningPointManger,
    private val userManager: UserManager,
) {
    @Transactional
    fun start(
        userId: Long,
        request: RunningStartRequest,
    ): RunningStartResponse {
        val user = userManager.getActiveUser(userId)
        val startAt = TimeProvider.parse(request.timeStamp)
        val runningRecord = runningRecordManager.start(user, startAt)
        runningPointManger.saveRunningPoints(runningRecord, request.lat, request.lon, startAt)
        return RunningStartResponse(runningRecord.id)
    }

    @Transactional
    fun update(
        userId: Long,
        recordId: Long,
        request: RunningUpdateRequest,
    ) {
        val user = userManager.getActiveUser(userId)
        val startAt = TimeProvider.parse(request.startAt)
        val runningRecord = runningRecordManager.getRunningRecord(id = recordId, user = user)
        // TODO
    }

    @Transactional
    fun pollingUpdate(
        userId: Long,
        recordId: Long,
        request: RunningPollingUpdateRequest,
    ): RunningPointResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.getRunningRecord(recordId, user = user)
        val newRunningPoint =
            runningPointManger.saveNewRunningPoints(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                heartRate = request.heartRate,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTime = request.totalRunningTime,
            )
        runningRecordManager.updateRecord(runningRecord)
        return RunningPointResponse(newRunningPoint)
    }

    @Transactional
    fun pause(
        userId: Long,
        recordId: Long,
        request: RunningPauseRequest,
    ): RunningPauseResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.stop(recordId, user = user)
        return RunningPauseResponse(userId, runningRecord.id)
    }

    @Transactional
    fun done(
        userId: Long,
        recordId: Long,
        request: RunningDoneRequest,
    ): RunningDoneResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.getRunningRecord(recordId, user = user)
        val runningPoints = runningPointManger.getRunningPoints(runningRecord)
        val finishRunningRecord = runningRecordManager.finish(recordId, user, runningPoints)
        return RunningDoneResponse(finishRunningRecord)
    }
}
