package com.yapp.yapp.record.domain

import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.user.domain.UserManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class RunningRecordService(
    private val recordManager: RunningRecordManager,
    private val pointManager: RunningPointManger,
    private val userManager: UserManager,
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
        val runningRecordResponse =
            recordManager.getRunningRecords(
                user = user,
                searchType = searchType,
                targetDate = targetDate,
                pageable = pageable,
            ).map { RunningRecordResponse(it, pointManager.getRunningPoints(it)) }
        return RunningRecordListResponse(userId = user.id, records = runningRecordResponse)
    }
}
