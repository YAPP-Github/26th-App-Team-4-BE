package com.yapp.yapp.record.api

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.record.api.response.RunningRecordListResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.record.domain.RecordService
import com.yapp.yapp.record.domain.RecordsSearchType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/records")
class RunningRecordController(
    private val recordService: RecordService,
) {
    @GetMapping
    fun getRunningRecords(
        @CurrentUser userId: Long,
        @RequestParam("type") type: String = RecordsSearchType.ALL.name,
        @RequestParam("targetDate") targetDate: String = TimeProvider.now().toString(),
        @PageableDefault(size = 10, sort = ["startAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
    ): ApiResponse<RunningRecordListResponse> {
        return ApiResponse.success(recordService.getRecords(userId, type, TimeProvider.parse(targetDate), pageable))
    }

    @GetMapping("/{recordId}")
    fun getRunningRecord(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
    ): ApiResponse<RunningRecordResponse> {
        return ApiResponse.success(recordService.getRecord(userId, recordId))
    }
}
