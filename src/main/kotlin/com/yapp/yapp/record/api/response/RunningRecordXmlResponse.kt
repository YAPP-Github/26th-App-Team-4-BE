package com.yapp.yapp.record.api.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
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
    val averagePace: Long,
) {
    constructor(runningRecordResponse: RunningRecordResponse) : this(
        recordId = runningRecordResponse.recordId,
        userId = runningRecordResponse.userId,
        runningPoints = runningRecordResponse.runningPoints.map { RunningPointXmlResponse(it) },
        totalDistance = runningRecordResponse.totalDistance,
        totalTime = runningRecordResponse.totalTime,
        totalCalories = runningRecordResponse.totalCalories,
        startAt = runningRecordResponse.startAt,
        averagePace = runningRecordResponse.averagePace,
    )
}
