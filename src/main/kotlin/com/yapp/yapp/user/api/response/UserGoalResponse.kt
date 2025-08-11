package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.RunnerType
import com.yapp.yapp.user.domain.goal.UserGoal

data class UserGoalResponse(
    val goalId: Long,
    val userId: Long,
    val runningPurpose: String? = null,
    val weeklyRunningCount: Int? = null,
    val paceGoal: Long? = null,
    val distanceMeterGoal: Double? = null,
    val timeGoal: Long? = null,
    val runnerType: RunnerType? = null,
) {
    constructor(userGoal: UserGoal) : this(
        goalId = userGoal.id,
        userId = userGoal.user.id,
        runningPurpose = userGoal.runningPurpose,
        weeklyRunningCount = userGoal.weeklyRunningCount,
        paceGoal = userGoal.paceGoal?.toMills(),
        distanceMeterGoal = userGoal.distanceMeterGoal,
        timeGoal = userGoal.timeGoal,
        runnerType = userGoal.user.runnerType,
    )
}
