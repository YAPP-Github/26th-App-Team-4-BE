package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.goal.RunningPurposeAnswerLabel
import com.yapp.yapp.user.domain.goal.UserGoal
import com.yapp.yapp.user.domain.goal.UserGoalRepository
import org.springframework.stereotype.Component

@Component
class UserGoalFixture(
    private val userGoalRepository: UserGoalRepository,
) {
    fun create(
        user: User,
        distanceMeterGoal: Double = 5000.0,
        timeGoal: Long = TimeProvider.toMills(minute = 40, second = 30),
        weeklyRunCount: Int = 3,
        paceGoal: Pace = Pace(distanceMeter = 5000.0, durationMills = TimeProvider.toMills(minute = 40, second = 30)),
        runningPurpose: RunningPurposeAnswerLabel = RunningPurposeAnswerLabel.WEIGHT_LOSS_PURPOSE,
    ): UserGoal {
        return userGoalRepository.save(
            UserGoal(
                user = user,
                distanceMeterGoal = distanceMeterGoal,
                timeGoal = timeGoal,
                weeklyRunCount = weeklyRunCount,
                paceGoal = paceGoal,
                runningPurpose = runningPurpose.name,
            ),
        )
    }
}
