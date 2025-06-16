package com.yapp.yapp.running.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/v1/running")
class RunningController {
    @PostMapping("/start")
    fun start() {
    }
}
