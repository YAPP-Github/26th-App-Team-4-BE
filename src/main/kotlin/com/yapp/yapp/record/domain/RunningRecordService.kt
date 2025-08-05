package com.yapp.yapp.record.domain

import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.record.domain.record.goal.RunningRecordGoalAchieveManager
import com.yapp.yapp.user.domain.UserManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class RunningRecordService(
    private val recordManager: RunningRecordManager,
    private val pointManager: RunningPointManger,
    private val userManager: UserManager,
    private val recordGoalAchieveManager: RunningRecordGoalAchieveManager,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ): RunningRecordResponse {
        val user = userManager.getActiveUser(userId)
        val runningRecord = recordManager.getRunningRecord(id = recordId, user = user)
        val runningPoints = pointManager.getRunningPoints(runningRecord)
        val runningGoalAchieved = recordGoalAchieveManager.getByRecord(runningRecord)
        return RunningRecordResponse(runningRecord, runningPoints, runningGoalAchieved)
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
            runningRecords.map {
                RunningRecordResponse(
                    runningRecord = it,
                    runningPoints = pointManager.getRunningPoints(it),
                    runningRecordGoalAchieve = recordGoalAchieveManager.getByRecord(it),
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
