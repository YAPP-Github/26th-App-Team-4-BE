package com.yapp.yapp.running.domain

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.common.storage.FilePathGenerator
import com.yapp.yapp.common.storage.StorageManager
import com.yapp.yapp.record.api.response.RunningPointResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.record.domain.record.goal.RunningRecordGoalAchieveManager
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPollingPauseRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.response.RunningPollingDoneResponse
import com.yapp.yapp.running.api.response.RunningPollingPauseResponse
import com.yapp.yapp.running.api.response.RunningRecordImageResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.user.domain.UserManager
import com.yapp.yapp.user.domain.goal.UserGoalManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class RunningService(
    private val runningRecordManager: RunningRecordManager,
    private val runningPointManger: RunningPointManger,
    private val userManager: UserManager,
    private val storageManager: StorageManager,
    private val userGoalManager: UserGoalManager,
    private val recordGoalArchiveManager: RunningRecordGoalAchieveManager,
) {
    @Transactional
    fun start(
        userId: Long,
        request: RunningStartRequest,
    ): RunningStartResponse {
        val user = userManager.getActiveUser(userId)
        val startAt = TimeProvider.parse(request.timeStamp)
        val runningRecord = runningRecordManager.start(user, startAt)
        runningPointManger.saveRunningPoint(runningRecord, request.lat, request.lon, startAt)
        recordGoalArchiveManager.save(runningRecord = runningRecord)
        return RunningStartResponse(runningRecord.id)
    }

    @Transactional
    fun done(
        userId: Long,
        recordId: Long,
        request: RunningDoneRequest,
    ): RunningRecordResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.getRunningRecord(id = recordId, user = user)
        if (request.runningPoints.isEmpty()) {
            throw CustomException(ErrorCode.POINT_NOT_FOUND)
        }
        val runningPoints =
            request.runningPoints.map {
                runningPointManger.saveNewRunningPoint(
                    runningRecord = runningRecord,
                    lat = it.lat,
                    lon = it.lon,
                    totalRunningTimeMills = it.totalRunningTimeMills,
                    timeStamp = TimeProvider.parse(it.timeStamp),
                )
            }

        if (userGoalManager.hasUserGoal(user)) {
            recordGoalArchiveManager.update(runningRecord, userGoal = userGoalManager.getUserGoal(user))
        }
        runningRecordManager.updateRecord(runningRecord)

        return RunningRecordResponse(
            runningRecord = runningRecord,
            runningPoints = runningPoints,
            runningRecordGoalAchieve = recordGoalArchiveManager.getByRecord(runningRecord),
        )
    }

    @Transactional
    fun uploadRecordImage(
        userId: Long,
        recordId: Long,
        imageFile: MultipartFile,
    ): RunningRecordImageResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.getRunningRecord(id = recordId, user = user)
        val filePath = FilePathGenerator.generateRunningRecordImagePath(runningRecord)
        val uploadFile = storageManager.uploadFile(filePath = filePath, multipartFile = imageFile)
        runningRecord.update(imageUrl = uploadFile.url)
        return RunningRecordImageResponse(
            userId = user.id,
            recordId = runningRecord.id,
            imageUrl = runningRecord.imageUrl,
        )
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
            runningPointManger.saveNewRunningPoint(
                runningRecord = runningRecord,
                lat = request.lat,
                lon = request.lon,
                timeStamp = TimeProvider.parse(request.timeStamp),
                totalRunningTimeMills = request.totalRunningTime,
            )
        runningRecordManager.updateRecord(runningRecord)
        return RunningPointResponse(newRunningPoint)
    }

    @Transactional
    fun pause(
        userId: Long,
        recordId: Long,
        request: RunningPollingPauseRequest,
    ): RunningPollingPauseResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.stop(recordId, user = user)
        return RunningPollingPauseResponse(userId, runningRecord.id)
    }

    @Transactional
    fun oldDone(
        userId: Long,
        recordId: Long,
    ): RunningPollingDoneResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = runningRecordManager.getRunningRecord(recordId, user = user)
        val runningPoints = runningPointManger.getRunningPoints(runningRecord)
        val finishRunningRecord = runningRecordManager.finish(recordId, user, runningPoints)
        return RunningPollingDoneResponse(finishRunningRecord)
    }
}
