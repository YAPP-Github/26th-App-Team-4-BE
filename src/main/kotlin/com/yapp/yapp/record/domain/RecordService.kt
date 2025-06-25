package com.yapp.yapp.record.domain

import com.yapp.yapp.record.RecordManager
import org.springframework.stereotype.Service

@Service
class RecordService(
    private val recordManager: RecordManager,
) {
    fun getRecord(
        userId: Long,
        recordId: Long,
    ) {
        val runningRecord = recordManager.getRunningRecord(recordId)
    }
}
