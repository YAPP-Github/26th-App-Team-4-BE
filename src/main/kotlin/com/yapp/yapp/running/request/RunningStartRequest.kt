package com.yapp.yapp.running.request

data class RunningStartRequest(
    val userId: Long,
    val lat: Double,
    val lon: Double,
)
