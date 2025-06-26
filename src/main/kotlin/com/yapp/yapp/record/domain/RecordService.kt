package com.yapp.yapp.record.domain

import com.yapp.yapp.record.api.response.RecordListResponse
import com.yapp.yapp.record.api.response.RecordResponse
import com.yapp.yapp.running.domain.record.RunningRecordManager
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class RecordService(
    private val recordManager: RunningRecordManager,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ) = RecordResponse(recordManager.getRunningRecord(id = recordId, userId = userId))

    fun getRecords(
        userId: Long,
        type: String,
        targetDate: OffsetDateTime,
        pageable: Pageable,
    ): RecordListResponse {
        val searchType = RecordsSearchType.getBy(type)
        val recordResponses =
            recordManager.getRunningRecords(
                userId = userId,
                type = searchType,
                targetDate = targetDate,
                pageable = pageable,
            ).map { RecordResponse(it) }
        return RecordListResponse(userId = userId, records = recordResponses)
    }
}
