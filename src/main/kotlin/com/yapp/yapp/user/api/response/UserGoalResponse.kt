package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.UserGoal
import java.time.Duration

data class UserGoalResponse(
    val id: Long,
    val userId: Long,
    var runningPurpose: String? = null,
    var weeklyRunCount: Int? = null,
    var paceGoal: Duration? = null,
    var distanceMeterGoal: Double? = null,
    var timeGoal: Duration? = null,
) {
    constructor(userGoal: UserGoal) : this(
        id = userGoal.id,
        userId = userGoal.user.id,
        runningPurpose = userGoal.runningPurpose,
        weeklyRunCount = userGoal.weeklyRunCount,
        paceGoal = userGoal.paceGoal?.pacePerKm,
        distanceMeterGoal = userGoal.distanceMeterGoal,
        timeGoal = userGoal.timeGoal,
    )
}
