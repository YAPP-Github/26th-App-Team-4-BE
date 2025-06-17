package com.yapp.yapp.running.api.request

import java.time.OffsetDateTime

data class RunningStartRequest(
    val userId: Long,
    val lat: Double,
    val lon: Double,
    val timeStamp: OffsetDateTime,
)
