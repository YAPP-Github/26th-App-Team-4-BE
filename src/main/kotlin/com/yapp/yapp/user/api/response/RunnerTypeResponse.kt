package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.RunnerType

data class RunnerTypeResponse(
    val userId: Long,
    val runnerType: String,
) {
    constructor(userId: Long, runnerType: RunnerType) : this(
        userId = userId,
        runnerType = runnerType.description,
    )
}
