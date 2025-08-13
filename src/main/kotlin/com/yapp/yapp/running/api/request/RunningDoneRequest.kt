package com.yapp.yapp.running.api.request

data class RunningDoneRequest(
    val totalTime: Long,
    val totalDistance: Double,
    val totalCalories: Int,
    val averagePace: Long,
    val startAt: String,
    val runningPoints: List<RunningPointRequest>,
) {
    data class RunningPointRequest(
        val lat: Double,
        val lon: Double,
        val totalRunningTimeMills: Long,
        val timeStamp: String,
    )
}
