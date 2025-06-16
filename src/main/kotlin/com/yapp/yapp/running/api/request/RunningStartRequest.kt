package com.yapp.yapp.running.api.request

data class RunningStartRequest(
    val userId: Long,
    val lat: Double,
    val lon: Double,
)
