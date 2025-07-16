package com.yapp.yapp.user.api.response

import com.yapp.yapp.record.domain.Pace

data class RecommendPaceResponse(
    val userId: Long,
    val recommendPace: Long,
) {
    constructor(userId: Long, recommendPace: Pace) : this(
        userId = userId,
        recommendPace = recommendPace.millsPerKm,
    )
}
