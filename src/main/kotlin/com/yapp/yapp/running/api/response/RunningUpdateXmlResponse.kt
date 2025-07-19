package com.yapp.yapp.running.api.response

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.yapp.yapp.record.api.response.RunningPointResponse
import com.yapp.yapp.record.api.response.RunningPointXmlResponse

data class RunningUpdateXmlResponse(
    @JacksonXmlProperty(localName = "recordId")
    val recordId: Long,
    @JacksonXmlProperty(localName = "userId")
    val userId: Long,
    @JacksonXmlElementWrapper(localName = "runningPoints", useWrapping = false)
    @JacksonXmlProperty(localName = "runningPoint")
    val runningPoint: RunningPointXmlResponse,
) {
    constructor(runningPointResponse: RunningPointResponse) : this(
        recordId = runningPointResponse.recordId,
        userId = runningPointResponse.userId,
        runningPoint = RunningPointXmlResponse(runningPointResponse),
    )
}
