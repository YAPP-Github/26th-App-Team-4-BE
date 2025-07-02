package com.yapp.yapp.running.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.common.web.ApiXmlResponse
import com.yapp.yapp.record.api.response.RunningPointResponse
import com.yapp.yapp.record.api.response.RunningPointXmlResponse
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.request.RunningUpdateXmlRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningPauseResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.domain.RunningService
import org.springframework.http.MediaType
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

    @PostMapping("/{recordId}")
    fun update(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningUpdateRequest,
    ): ApiResponse<RunningPointResponse> {
        return ApiResponse.success(runningService.update(userId, recordId, request))
    }

    @PostMapping(value = ["/{recordId}"], produces = [MediaType.APPLICATION_XML_VALUE])
    fun updateXml(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningUpdateXmlRequest,
    ): ApiXmlResponse<RunningPointXmlResponse> {
        val pointResponse = runningService.update(userId, recordId, request.toJson())
        return ApiXmlResponse.success(RunningPointXmlResponse(pointResponse))
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
        @RequestBody request: RunningDoneRequest,
    ): ApiResponse<RunningDoneResponse> {
        return ApiResponse.success(runningService.done(userId, recordId, request))
    }
}
