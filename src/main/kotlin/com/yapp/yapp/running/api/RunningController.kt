package com.yapp.yapp.running.api

import com.yapp.yapp.running.RunningService
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.running.api.response.RunningStartResponse
import com.yapp.yapp.running.api.response.RunningUpdateResponse
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
        @RequestBody request: RunningStartRequest,
    ): RunningStartResponse {
        return runningService.start(request)
    }

    @PostMapping("/update")
    fun start(
        @RequestBody request: RunningUpdateRequest,
    ): RunningUpdateResponse {
        return runningService.update(request)
    }
}
