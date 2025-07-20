package com.yapp.yapp.running.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningPauseResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
import com.yapp.yapp.running.domain.RunningService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/running")
class RunningController(
    private val runningService: RunningService,
) {
    @PostMapping
    fun start(
        @CurrentUser userId: Long,
        @RequestBody request: RunningStartRequest,
    ): ApiResponse<RunningStartResponse> {
        return ApiResponse.success(runningService.start(userId, request))
    }

    @PostMapping("/{recordId}/polling")
    fun pollingUpdate(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningPollingUpdateRequest,
    ): ApiResponse<RunningUpdateResponse> {
        val pointResponse = runningService.pollingUpdate(userId, recordId, request)
        return ApiResponse.success(RunningUpdateResponse(pointResponse))
    }

    @PostMapping("/{recordId}")
    fun save(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningPollingUpdateRequest,
    ): RunningRecordResponse {
        TODO()
    }

    @PatchMapping("/{recordId}")
    fun pause(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningPauseRequest,
    ): ApiResponse<RunningPauseResponse> {
        return ApiResponse.success(runningService.pause(userId, recordId, request))
    }

    @PostMapping("/{recordId}/done")
    fun done(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
    ): ApiResponse<RunningDoneResponse> {
        return ApiResponse.success(runningService.oldDone(userId, recordId))
    }
}
