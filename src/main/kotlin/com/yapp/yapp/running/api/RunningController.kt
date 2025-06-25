package com.yapp.yapp.running.api

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningResumeRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningStopRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningResumeResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningStopResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
import com.yapp.yapp.running.domain.RunningService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/running")
class RunningController(
    private val runningService: RunningService,
) {
    @PostMapping("/start")
    fun start(
        @CurrentUser userId: Long,
        @RequestBody request: RunningStartRequest,
    ): ApiResponse<RunningStartResponse> {
        return ApiResponse.success(runningService.start(userId, request))
    }

    @PostMapping("/update")
    fun update(
        @CurrentUser userId: Long,
        @RequestBody request: RunningUpdateRequest,
    ): ApiResponse<RunningUpdateResponse> {
        return ApiResponse.success(runningService.update(request))
    }

    @PatchMapping("/stop")
    fun stop(
        @CurrentUser userId: Long,
        @RequestBody request: RunningStopRequest,
    ): ApiResponse<RunningStopResponse> {
        return ApiResponse.success(runningService.stop(userId, request))
    }

    @PostMapping("/resume")
    fun resume(
        @CurrentUser userId: Long,
        @RequestBody request: RunningResumeRequest,
    ): ApiResponse<RunningResumeResponse> {
        return ApiResponse.success(runningService.resume(request))
    }

    @PostMapping("/done")
    fun done(
        @CurrentUser userId: Long,
        @RequestBody request: RunningDoneRequest,
    ): ApiResponse<RunningDoneResponse> {
        return ApiResponse.success(runningService.done(request))
    }
}
