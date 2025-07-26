package com.yapp.yapp.running.api.request

data class RunningPollingUpdateRequest(
    val lat: Double,
    val lon: Double,
    val heartRate: Int?,
    val totalRunningTime: Long,
    val timeStamp: String,
)
