package com.yapp.yapp.user.api.response.goal

import com.yapp.yapp.user.api.response.UserGoalResponse

data class DistanceGoalResponse(
    val goalId: Long,
    val userId: Long,
    val distanceMeterGoal: Double,
) {
    constructor(userGoalResponse: UserGoalResponse) : this(
        goalId = userGoalResponse.goalId,
        userId = userGoalResponse.userId,
        distanceMeterGoal = userGoalResponse.distanceMeterGoal as Double,
    )
}
