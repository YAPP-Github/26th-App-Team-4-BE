package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.point.RunningPoint
import kotlin.collections.forEach

data class SegmentListResponse(
    val segmentList: List<SegmentResponse>,
) {
    companion object {
        private const val DIV_DISTANCE = 1000.0

        fun of(runningPoints: List<RunningPoint>): SegmentListResponse {
            val segments = mutableListOf<SegmentResponse>()
            var nextDistance = DIV_DISTANCE
            var segmentOrder = 1

            runningPoints.forEach { point ->
                if (point.totalRunningDistance >= nextDistance) {
                    segments.add(
                        SegmentResponse(
                            orderNo = segmentOrder++,
                            distanceMeter = point.totalRunningDistance,
                            averagePace = Pace(distanceMeter = point.totalRunningDistance, durationMills = point.totalRunningTime).toMills(),
                        ),
                    )
                    nextDistance += DIV_DISTANCE
                }
            }

            return SegmentListResponse(segments)
        }
    }
}
