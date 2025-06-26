package com.yapp.yapp.record.api

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.record.api.response.RecordListResponse
import com.yapp.yapp.record.api.response.RecordResponse
import com.yapp.yapp.record.domain.RecordService
import com.yapp.yapp.record.domain.RecordsSearchType
import jakarta.websocket.server.PathParam
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
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
    fun getRunningRecords(
        @CurrentUser userId: Long,
        @PathParam("type") type: String = RecordsSearchType.ALL.name,
        @PathParam("targetDate") targetDate: String = TimeProvider.now().toString(),
        @PageableDefault(size = 10, sort = ["startAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
    ): ApiResponse<RecordListResponse> {
        return ApiResponse.success(recordService.getRecords(userId, type, TimeProvider.parse(targetDate), pageable))
    }

    @GetMapping("/{recordId}")
    fun getRunningRecord(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
    ): ApiResponse<RecordResponse> {
        return ApiResponse.success(recordService.getRecord(userId, recordId))
    }
}
