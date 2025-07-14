package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.goal.UserGoal

data class UserGoalResponse(
    val id: Long,
    val userId: Long,
    val runningPurpose: String? = null,
    val weeklyRunningCount: Int? = null,
    val paceGoal: Long? = null,
    val distanceMeterGoal: Double? = null,
    val timeGoal: Long? = null,
) {
    constructor(userGoal: UserGoal) : this(
        id = userGoal.id,
        userId = userGoal.user.id,
        runningPurpose = userGoal.runningPurpose,
        weeklyRunningCount = userGoal.weeklyRunningCount,
        paceGoal = userGoal.paceGoal?.toMills(),
        distanceMeterGoal = userGoal.distanceMeterGoal,
        timeGoal = userGoal.timeGoal,
    )
}
