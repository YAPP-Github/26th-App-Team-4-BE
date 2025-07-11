package com.yapp.yapp.home.api

import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserGoal

data class HomeResponse(
    val user: UserResponse,
    val record: RecordResponse,
    val userGoal: GoalResponse,
) {
    constructor(user: User, userGoal: UserGoal, recentRecord: RunningRecord, totalRecord: RunningRecord, thisWeekRunCount: Int) :
        this(
            user = UserResponse(userId = user.id, nickname = user.nickname),
            userGoal =
                GoalResponse(
                    runningGoal = userGoal.runningGoal,
                    weeklyRunCount = userGoal.weeklyRunCount,
                    paceGoal = userGoal.paceGoal?.pacePerKm?.toMillis(),
                    distanceMeterGoal = userGoal.distanceMeterGoal,
                    timeGoal = userGoal.timeGoal?.toMillis(),
                ),
            record =
                RecordResponse(
                    totalDistance = totalRecord.totalDistance,
                    thisWeekRunCount = thisWeekRunCount,
                    recentPace = recentRecord.averagePace.pacePerKm.toMillis(),
                    recentDistanceMeter = recentRecord.totalDistance,
                    recentTime = recentRecord.totalTime.toMillis(),
                ),
        )

    data class UserResponse(
        val userId: Long,
        val nickname: String,
    )

    data class RecordResponse(
        val totalDistance: Double,
        val thisWeekRunCount: Int,
        val recentPace: Long,
        val recentDistanceMeter: Double,
        val recentTime: Long,
    )

    data class GoalResponse(
        var runningGoal: String? = null,
        var weeklyRunCount: Int? = null,
        var paceGoal: Long? = null,
        var distanceMeterGoal: Double? = null,
        var timeGoal: Long? = null,
    )
}
