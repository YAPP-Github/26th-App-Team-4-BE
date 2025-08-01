package com.yapp.yapp.record.domain

import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.user.domain.UserManager
import com.yapp.yapp.user.domain.goal.UserGoalManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class RunningRecordService(
    private val recordManager: RunningRecordManager,
    private val pointManager: RunningPointManger,
    private val userManager: UserManager,
    private val userGoalManager: UserGoalManager,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ): RunningRecordResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = recordManager.getRunningRecord(id = recordId, user = user)
        val runningPoints = pointManager.getRunningPoints(runningRecord)
        return RunningRecordResponse(runningRecord, runningPoints)
    }

    fun getRecords(
        userId: Long,
        type: String,
        targetDate: OffsetDateTime,
        pageable: Pageable,
    ): RunningRecordListResponse {
        val user = userManager.getActiveUser(userId)
        val searchType = RecordsSearchType.getBy(type)
        val runningRecords =
            recordManager.getRunningRecords(
                user = user,
                searchType = searchType,
                targetDate = targetDate,
                pageable = pageable,
            )
        val runningRecordResponse =
            runningRecords.map { RunningRecordResponse(it, pointManager.getRunningPoints(it)) }
        if (userGoalManager.hasUserGoal(user)) {
            val userGoal = userGoalManager.getUserGoal(user)
            val timeGoalAchievedCount = runningRecords.count { userGoal.isTimeGoalAchieved(it) }
            val distanceGoalAchievedCount = runningRecords.count { userGoal.isDistanceGoalAchieved(it) }
            return RunningRecordListResponse(
                userId = user.id,
                records = runningRecordResponse,
                timeGoalAchievedCount = timeGoalAchievedCount,
                distanceGoalAchievedCount = distanceGoalAchievedCount,
            )
        }
        return RunningRecordListResponse(
            userId = user.id,
            records = runningRecordResponse,
        )
    }

    @Transactional
    fun deleteRecord(
        userId: Long,
        recordId: Long,
    ) {
        val user = userManager.getActiveUser(userId)
        val record = recordManager.getRunningRecord(id = recordId, user = user)
        record.delete()
    }
}
