package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

class PaceGoalResponse(
    val goalId: Long,
    val userId: Long,
    val paceGoal: Long,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
        paceGoal = userGoalResponse.paceGoal as Long,
    )
}
