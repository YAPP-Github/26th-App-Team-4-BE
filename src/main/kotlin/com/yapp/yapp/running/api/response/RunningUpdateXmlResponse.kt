package com.yapp.yapp.running.api.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.Duration
import java.time.OffsetDateTime

@JacksonXmlRootElement(localName = "runningUpdate") // or 원하는 엘리먼트 이름
data class RunningUpdateXmlResponse(
    @JacksonXmlProperty(localName = "runningPointId")
    val runningPointId: Long,
    @JacksonXmlProperty(localName = "userId")
    val userId: Long,
    @JacksonXmlProperty(localName = "recordId")
    val recordId: Long,
    @JacksonXmlProperty(localName = "orderNo")
    val orderNo: Long,
    @JacksonXmlProperty(localName = "waypoint")
    val waypoint: WaypointResponse,
    @JacksonXmlProperty(localName = "speed")
    val speed: Double,
    @JacksonXmlProperty(localName = "distance")
    val distance: Double,
    @JacksonXmlProperty(localName = "pace")
    val pace: Duration,
    @JacksonXmlProperty(localName = "heartRate")
    val heartRate: Int,
    @JacksonXmlProperty(localName = "calories")
    val calories: Int,
    @JacksonXmlProperty(localName = "timeStamp")
    val timeStamp: OffsetDateTime,
)

data class WaypointResponse(
    @JacksonXmlProperty(localName = "lat", isAttribute = true)
    val lat: Double,
    @JacksonXmlProperty(localName = "lon", isAttribute = true)
    val lon: Double,
)
