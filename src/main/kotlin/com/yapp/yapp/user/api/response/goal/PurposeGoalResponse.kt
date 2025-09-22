package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

data class PurposeGoalResponse(
    val goalId: Long,
    val userId: Long,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
    )
}
