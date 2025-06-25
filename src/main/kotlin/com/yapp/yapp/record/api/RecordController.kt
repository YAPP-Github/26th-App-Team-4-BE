package com.yapp.yapp.record.api

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.record.api.response.RecordResponse
import com.yapp.yapp.record.domain.RecordService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/records")
class RecordController(
    private val recordService: RecordService,
) {
    @GetMapping
    fun getRecords(
        @CurrentUser userId: Long,
    ) {
        TODO("전체, 월간, 주간, 일간 기록을 가져온다")
    }

    @GetMapping("{recordId}")
    fun getRecord(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
    ): ApiResponse<RecordResponse> {
        return ApiResponse.success(recordService.getRecord(userId, recordId))
    }
}
