package com.yapp.yapp.record.domain

import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.point.RunningPointManger
import com.yapp.yapp.record.domain.record.RunningRecordManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class RunningRecordService(
    private val recordManager: RunningRecordManager,
    private val pointManager: RunningPointManger,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ): RunningRecordResponse {
        val runningRecord = recordManager.getRunningRecord(id = recordId, userId = userId)
        val runningPoints = pointManager.getRunningPoints(runningRecord)
        return RunningRecordResponse(runningRecord, runningPoints)
    }

    fun getRecords(
        userId: Long,
        type: String,
        targetDate: OffsetDateTime,
        pageable: Pageable,
    ): RunningRecordListResponse {
        val searchType = RecordsSearchType.getBy(type)
        val runningRecordResponse =
            recordManager.getRunningRecords(
                userId = userId,
                type = searchType,
                targetDate = targetDate,
                pageable = pageable,
            ).map { RunningRecordResponse(it, pointManager.getRunningPoints(it)) }
        return RunningRecordListResponse(userId = userId, records = runningRecordResponse)
    }
}
