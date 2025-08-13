package com.yapp.yapp.running.api.request

data class RunningStartRequest(
    val lat: Double,
    val lon: Double,
    val timeStamp: String,
)
