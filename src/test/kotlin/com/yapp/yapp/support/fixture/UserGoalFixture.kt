package com.yapp.yapp.support.fixture

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserGoal
import com.yapp.yapp.user.domain.UserGoalRepository
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
    ): UserGoal {
        return userGoalRepository.save(
            UserGoal(
                user = user,
                distanceMeterGoal = distanceMeterGoal,
                timeGoal = timeGoal,
                weeklyRunCount = weeklyRunCount,
                paceGoal = paceGoal,
            ),
        )
    }
}
