package com.yapp.yapp.running.api

import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.record.api.response.RunningRecordResponse
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPollingPauseRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.response.RunningPollingDoneResponse
import com.yapp.yapp.running.api.response.RunningPollingPauseResponse
import com.yapp.yapp.running.api.response.RunningPollingUpdateResponse
import com.yapp.yapp.running.api.response.RunningRecordImageResponse
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.domain.RunningService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

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
    fun done(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningDoneRequest,
    ): ApiResponse<RunningRecordResponse> {
        val recordResponse =
            runningService.doneBatch(
                userId = userId,
                recordId = recordId,
                request = request,
            )
        return ApiResponse.success(recordResponse)
    }

    @PostMapping("/{recordId}/images")
    fun uploadRecordImage(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestPart("image") imageFile: MultipartFile,
    ): ApiResponse<RunningRecordImageResponse> {
        val imageResponse =
            runningService.uploadRecordImage(
                userId = userId,
                recordId = recordId,
                imageFile = imageFile,
            )
        return ApiResponse.success(imageResponse)
    }

    @PostMapping("/{recordId}/polling")
    fun pollingUpdate(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningPollingUpdateRequest,
    ): ApiResponse<RunningPollingUpdateResponse> {
        val pointResponse = runningService.pollingUpdate(userId, recordId, request)
        return ApiResponse.success(RunningPollingUpdateResponse(pointResponse))
    }

    @PatchMapping("/{recordId}")
    fun pollingPause(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
        @RequestBody request: RunningPollingPauseRequest,
    ): ApiResponse<RunningPollingPauseResponse> {
        return ApiResponse.success(runningService.pause(userId, recordId, request))
    }

    @PostMapping("/{recordId}/done")
    fun pollingDone(
        @CurrentUser userId: Long,
        @PathVariable recordId: Long,
    ): ApiResponse<RunningPollingDoneResponse> {
        return ApiResponse.success(runningService.oldDone(userId, recordId))
    }
}
