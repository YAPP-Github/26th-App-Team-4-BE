package com.yapp.yapp.running.api.request

data class RunningUpdateRequest(
    val lat: Double,
    val lon: Double,
    val heartRate: Int?,
    val totalRunningTime: String,
    val timeStamp: String,
)
