package com.yapp.yapp.record.api.response

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.point.RunningPoint
import kotlin.collections.forEach

data class SegmentListResponse(
    val segmentList: List<SegmentResponse>,
) {
    companion object {
        private const val DIV_DISTANCE_METER = 1000.0

        fun of(runningPoints: List<RunningPoint>): SegmentListResponse {
            val segments = mutableListOf<SegmentResponse>()
            var nextDistanceMeter = DIV_DISTANCE_METER
            var segmentOrder = 1

            runningPoints.forEach { point ->
                if (point.totalRunningDistance >= nextDistanceMeter) {
                    segments.add(
                        SegmentResponse(
                            orderNo = segmentOrder++,
                            distanceMeter = point.totalRunningDistance,
                            averagePace = Pace(distanceMeter = point.totalRunningDistance, durationMills = point.totalRunningTime).toMills(),
                        ),
                    )
                    nextDistanceMeter += DIV_DISTANCE_METER
                }
            }

            return SegmentListResponse(segments)
        }
    }
}
