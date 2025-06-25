package com.yapp.yapp.support.fixture

import com.yapp.yapp.running.api.request.RunningUpdateRequest

object RequestFixture {
    fun runningUpdateRequest(
        recordId: Long = 1L,
        lat: Double = 37.5665,
        lon: Double = 126.9780,
        heartRate: Int = 142,
        totalRunningTime: String = "PT1M2.12S",
        timeStamp: String = "2025-06-17T16:12:00+09:00",
    ) = RunningUpdateRequest(
        lat = lat,
        lon = lon,
        heartRate = heartRate,
        totalRunningTime = totalRunningTime,
        timeStamp = timeStamp,
    )
}
