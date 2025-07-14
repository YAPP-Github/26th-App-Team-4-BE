package com.yapp.yapp.home.api.response

import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.goal.UserGoal

data class HomeResponse(
    val user: UserResponse,
    val record: RecordResponse,
    val userGoal: GoalResponse,
) {
    constructor(user: User, userGoal: UserGoal, recentRecord: RunningRecord?, totalRecord: RunningRecord, thisWeekRunningCount: Int) :
        this(
            user = UserResponse(userId = user.id, nickname = user.nickname),
            userGoal =
                GoalResponse(
                    runningPurpose = userGoal.runningPurpose,
                    weeklyRunningCount = userGoal.weeklyRunningCount,
                    paceGoal = userGoal.paceGoal?.toMills(),
                    distanceMeterGoal = userGoal.distanceMeterGoal,
                    timeGoal = userGoal.timeGoal,
                ),
            record =
                RecordResponse(
                    totalDistance = totalRecord.totalDistance,
                    thisWeekRunningCount = thisWeekRunningCount,
                    recentPace = recentRecord?.averagePace!!.toMills(),
                    recentDistanceMeter = recentRecord.totalDistance,
                    recentTime = recentRecord.totalTime,
                ),
        )

    data class UserResponse(
        val userId: Long,
        val nickname: String,
    )

    data class RecordResponse(
        val totalDistance: Double,
        val thisWeekRunningCount: Int,
        val recentPace: Long? = null,
        val recentDistanceMeter: Double? = null,
        val recentTime: Long? = null,
    )

    data class GoalResponse(
        var runningPurpose: String? = null,
        var weeklyRunningCount: Int? = null,
        var paceGoal: Long? = null,
        var distanceMeterGoal: Double? = null,
        var timeGoal: Long? = null,
    )
}
