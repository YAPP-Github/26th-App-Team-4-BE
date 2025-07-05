package com.yapp.yapp.running.api.response

import com.yapp.yapp.record.api.response.RunningPointResponse

data class RunningUpdateResponse(
    val recordId: Long,
    val userId: Long,
    val runningPoint: RunningPointResponse,
) {
    constructor(runningPointResponse: RunningPointResponse) : this(
        recordId = runningPointResponse.recordId,
        userId = runningPointResponse.userId,
        runningPoint = runningPointResponse,
    )
}
