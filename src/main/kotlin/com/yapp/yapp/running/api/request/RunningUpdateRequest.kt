package com.yapp.yapp.running.api.request

// @XmlRootElement(name = "runningUpdateRequest")
// @XmlAccessorType(XmlAccessType.FIELD)
data class RunningUpdateRequest(
//    @XmlElement(name = "lat")
    val lat: Double,
//    @XmlElement(name = "lon")
    val lon: Double,
//    @XmlElement(name = "heartRate")
    val heartRate: Int?,
//    @XmlElement(name = "totalRunningTime")
    val totalRunningTime: String,
//    @XmlElement(name = "timeStamp")
    val timeStamp: String,
) {
    constructor() : this(0.0, 0.0, null, "", "")
}
