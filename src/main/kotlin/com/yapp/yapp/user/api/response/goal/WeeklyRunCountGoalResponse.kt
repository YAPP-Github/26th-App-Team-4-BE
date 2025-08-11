package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

data class WeeklyRunCountGoalResponse(
    val goalId: Long,
    val userId: Long,
    val weeklyRunningCount: Int,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
        weeklyRunningCount = userGoalResponse.weeklyRunningCount as Int,
    )
}
