package com.yapp.yapp.record.api.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.point.RunningPoint
import java.time.Duration
import java.time.OffsetDateTime

@JacksonXmlRootElement(localName = "runningPoint")
data class RunningPointXmlResponse(
    @JacksonXmlProperty(localName = "runningPointId")
    val runningPointId: Long,
    @JacksonXmlProperty(localName = "userId")
    val userId: Long,
    @JacksonXmlProperty(localName = "recordId")
    val recordId: Long,
    @JacksonXmlProperty(localName = "orderNo")
    val orderNo: Long,
    @JacksonXmlProperty(localName = "lat", isAttribute = true)
    val lat: Double,
    @JacksonXmlProperty(localName = "lon", isAttribute = true)
    val lon: Double,
    @JacksonXmlProperty(localName = "speed")
    var speed: Double = 0.0,
    @JacksonXmlProperty(localName = "distance")
    var distance: Double = 0.0,
    @JacksonXmlProperty(localName = "pace")
    var pace: Duration = Pace(0).pacePerKm,
    @JacksonXmlProperty(localName = "heartRate")
    val heartRate: Int? = 0,
    @JacksonXmlProperty(localName = "calories")
    val calories: Int = 0,
    @JacksonXmlProperty(localName = "totalRunningTime")
    val totalRunningTime: Duration = Duration.ZERO,
    @JacksonXmlProperty(localName = "totalRunningDistance")
    var totalRunningDistance: Double = 0.0,
    @JacksonXmlProperty(localName = "timeStamp")
    val timeStamp: OffsetDateTime = TimeProvider.now(),
) {
    constructor(runningPoint: RunningPoint) : this(
        runningPointId = runningPoint.id,
        userId = runningPoint.runningRecord.userId,
        recordId = runningPoint.runningRecord.id,
        orderNo = runningPoint.orderNo,
        lat = runningPoint.lat,
        lon = runningPoint.lon,
        speed = runningPoint.speedKmh,
        distance = runningPoint.distance,
        pace = runningPoint.pace.pacePerKm,
        heartRate = runningPoint.heartRate,
        calories = runningPoint.calories,
        totalRunningTime = runningPoint.totalRunningTime,
        totalRunningDistance = runningPoint.totalRunningDistance,
        timeStamp = runningPoint.timeStamp,
    )

    constructor(runningPointResponse: RunningPointResponse) : this(
        runningPointId = runningPointResponse.runningPointId,
        userId = runningPointResponse.userId,
        recordId = runningPointResponse.recordId,
        orderNo = runningPointResponse.orderNo,
        lat = runningPointResponse.lat,
        lon = runningPointResponse.lon,
        speed = runningPointResponse.speed,
        distance = runningPointResponse.distance,
        pace = runningPointResponse.pace,
        heartRate = runningPointResponse.heartRate,
        calories = runningPointResponse.calories,
        totalRunningTime = runningPointResponse.totalRunningTime,
        totalRunningDistance = runningPointResponse.totalRunningDistance,
        timeStamp = runningPointResponse.timeStamp,
    )
}
