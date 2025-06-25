package com.yapp.yapp.record.api

import com.yapp.yapp.record.api.response.RecordResponse
import com.yapp.yapp.running.domain.record.RunningRecordManager
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/records")
class RecordController(
    private val runningRecordManager: RunningRecordManager,
) {
    fun getRunningRecord(): RecordResponse {
        TODO()
    }
}
