package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.RunnerType

data class RunnerTypeResponse(
    val userId: Long,
    val runnerType: RunnerType,
)
