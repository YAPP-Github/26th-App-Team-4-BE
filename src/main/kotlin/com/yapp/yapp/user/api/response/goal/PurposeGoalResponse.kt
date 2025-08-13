package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

data class PurposeGoalResponse(
    val goalId: Long,
    val userId: Long,
    val runningPurpose: String,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
        runningPurpose = userGoalResponse.runningPurpose as String,
    )
}
