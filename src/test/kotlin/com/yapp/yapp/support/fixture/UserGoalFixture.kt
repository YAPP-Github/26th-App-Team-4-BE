package com.yapp.yapp.support.fixture

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.goal.RunningPurposeAnswerLabel
import com.yapp.yapp.user.domain.goal.UserGoal
import com.yapp.yapp.user.domain.goal.UserGoalRepository
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserGoalFixture(
    private val userGoalRepository: UserGoalRepository,
) {
    fun create(
        user: User,
        distanceMeterGoal: Double = 5000.0,
        timeGoal: Duration = Duration.parse("PT40M30S"),
        weeklyRunCount: Int = 3,
        paceGoal: Pace = Pace(distanceMeter = 5000.0, duration = Duration.parse("PT40M30S")),
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
