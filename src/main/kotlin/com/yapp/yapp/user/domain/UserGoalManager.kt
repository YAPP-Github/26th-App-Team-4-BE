package com.yapp.yapp.user.domain

import com.yapp.yapp.record.domain.Pace
import org.springframework.stereotype.Component

@Component
class UserGoalManager(
    private val userGoalDao: UserGoalDao,
) {
    fun savePaceGoal(
        user: User,
        pace: Pace,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: UserGoal(user = user)
        userGoal.updatePaceGoal(pace)
        return userGoal
    }

    fun saveWeeklyRunCountGoal(
        user: User,
        weeklyRunCount: Int,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: userGoalDao.save(UserGoal(user = user))
        userGoal.updateWeeklyRunCount(weeklyRunCount)

        return userGoal
    }

    fun getUserGoal(user: User): UserGoal {
        return userGoalDao.getUserGoal(user)
    }
}
