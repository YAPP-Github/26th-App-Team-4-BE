package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

data class TimeGoalResponse(
    val goalId: Long,
    val userId: Long,
    val timeGoal: Long,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
        timeGoal = userGoalResponse.timeGoal as Long,
    )
}
