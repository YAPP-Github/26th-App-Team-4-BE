package com.yapp.yapp.record.api.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.yapp.yapp.record.domain.point.RunningPoint
import com.yapp.yapp.record.domain.record.RunningRecord
import java.time.OffsetDateTime

data class RunningRecordXmlResponse(
    val recordId: Long,
    val userId: Long,
    @JacksonXmlElementWrapper(localName = "runningPoints")
    @JacksonXmlProperty(localName = "runningPoint")
    val runningPoints: List<RunningPointXmlResponse>,
    val totalDistance: Double,
    val totalTime: Long,
    val totalCalories: Int,
    val startAt: OffsetDateTime,
    val averageSpeed: Double,
    val averagePace: Long,
) {
    constructor(runningRecord: RunningRecord, runningPoints: List<RunningPoint>) : this(
        recordId = runningRecord.id,
        userId = runningRecord.userId,
        runningPoints = runningPoints.map { RunningPointXmlResponse(it) },
        totalDistance = runningRecord.totalDistance,
        totalTime = runningRecord.totalTime.toMillis(),
        totalCalories = runningRecord.totalCalories,
        startAt = runningRecord.startAt,
        averageSpeed = runningRecord.averageSpeed,
        averagePace = runningRecord.averagePace.toMills(),
    )

    constructor(runningRecordResponse: RunningRecordResponse) : this(
        recordId = runningRecordResponse.recordId,
        userId = runningRecordResponse.userId,
        runningPoints = runningRecordResponse.runningPoints.map { RunningPointXmlResponse(it) },
        totalDistance = runningRecordResponse.totalDistance,
        totalTime = runningRecordResponse.totalTime,
        totalCalories = runningRecordResponse.totalCalories,
        startAt = runningRecordResponse.startAt,
        averageSpeed = runningRecordResponse.averageSpeed,
        averagePace = runningRecordResponse.averagePace,
    )
}
