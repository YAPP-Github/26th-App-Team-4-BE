package com.yapp.yapp.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/check/health")
    fun check(): String = "I'm OK"
}
