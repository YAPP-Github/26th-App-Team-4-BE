package com.yapp.yapp.running.api

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.ApiXmlResponse
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPauseRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningDoneResponse
import com.yapp.yapp.running.api.response.RunningPauseResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
import com.yapp.yapp.running.api.response.RunningUpdateXmlResponse
import com.yapp.yapp.running.domain.RunningService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.OffsetDateTime

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
    ): ApiResponse<RunningUpdateResponse> {
        return ApiResponse.success(runningService.update(userId, recordId, request))
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

    @GetMapping(value = arrayOf("/t"), produces = [MediaType.APPLICATION_XML_VALUE])
    fun t(): ApiXmlResponse<RunningUpdateXmlResponse> {
        return ApiXmlResponse.success(
            RunningUpdateXmlResponse(
                runningPointId = 1L,
                userId = 1L,
                recordId = 1L,
                orderNo = 1L,
                lat = 37.5665,
                lon = 126.9780,
                speed = 10.0,
                distance = 1000.0,
                pace = Duration.ofMinutes(5),
                heartRate = 120,
                calories = 200,
                timeStamp = OffsetDateTime.now(),
            ),
        )
    }
}
