package com.yapp.yapp.common.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/check/health")
    fun check(): ApiResponse<String> = ApiResponse.success("I'm OK")

    @GetMapping("/api/v1/check/error")
    fun error(): Unit = throw IllegalArgumentException("test error")
}
