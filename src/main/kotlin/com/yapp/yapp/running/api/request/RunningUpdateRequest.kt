package com.yapp.yapp.running.api.request

import java.time.OffsetDateTime
import kotlin.time.Duration

data class RunningUpdateRequest(
    val userId: Long,
    val recordId: Long,
    val lat: Double,
    val lon: Double,
    val heartRate: Int,
    val totalRunningTime: Duration,
    val timeStamp: OffsetDateTime,
)
