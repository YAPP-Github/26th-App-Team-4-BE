package com.yapp.yapp.running.api.request

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "runningUpdateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class RunningUpdateXmlRequest(
    @JacksonXmlProperty(localName = "lat")
    val lat: Double,
    @JacksonXmlProperty(localName = "lon")
    val lon: Double,
    @JacksonXmlProperty(localName = "heartRate")
    val heartRate: Int?,
    @JacksonXmlProperty(localName = "totalRunningTime")
    val totalRunningTime: String,
    @JacksonXmlProperty(localName = "timeStamp")
    val timeStamp: String,
) {
    constructor() : this(0.0, 0.0, null, "", "")

    constructor(runningUpdateRequest: RunningUpdateRequest) : this(
        lat = runningUpdateRequest.lat,
        lon = runningUpdateRequest.lon,
        heartRate = runningUpdateRequest.heartRate,
        totalRunningTime = runningUpdateRequest.totalRunningTime,
        timeStamp = runningUpdateRequest.timeStamp,
    )

    fun toJson(): RunningUpdateRequest {
        return RunningUpdateRequest(
            lat = lat,
            lon = lon,
            heartRate = heartRate,
            totalRunningTime = totalRunningTime,
            timeStamp = timeStamp,
        )
    }
}
