package com.yapp.yapp.record.domain

import com.yapp.yapp.record.domain.record.RunningRecordManager
import org.springframework.stereotype.Service

@Service
class RecordService(
    private val runningRecordManager: RunningRecordManager,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ) {
        TODO()
    }
}
