package com.yapp.yapp.running.api.request

data class RunningUpdateRequest(
    val totalTime: Long,
    val totalDistance: Double,
    val totalCalories: Int,
    val averagePace: Long,
    val startAt: String,
    val runningPoints: List<RunningPointRequest>,
    val segments: List<SegmentRequest>,
) {
    data class RunningPointRequest(
        val lat: Double,
        val lon: Double,
        val timeStamp: String,
    )

    data class SegmentRequest(
        val orderNo: Int,
        val distanceMeter: Double,
        val averagePace: Long,
    )
}
