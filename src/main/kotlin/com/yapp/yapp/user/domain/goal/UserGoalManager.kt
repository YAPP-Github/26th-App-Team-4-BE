package com.yapp.yapp.user.domain.goal

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.domain.User
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UserGoalManager(
    private val userGoalDao: UserGoalDao,
) {
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

    fun savePaceGoal(
        user: User,
        pace: Pace,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: userGoalDao.save(UserGoal(user = user))
        userGoal.updatePaceGoal(pace)
        return userGoal
    }

    fun saveDistanceGoal(
        user: User,
        distanceMeter: Double,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: userGoalDao.save(UserGoal(user = user))
        userGoal.updateDistanceMeterGoal(distanceMeter)
        return userGoal
    }

    fun saveTimeGoal(
        user: User,
        time: Duration,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: userGoalDao.save(UserGoal(user = user))
        userGoal.updateTimeGoal(time)
        return userGoal
    }

    fun saveRunningPurpose(
        user: User,
        runningPurpose: RunningPurposeAnswerLabel,
    ): UserGoal {
        val userGoal =
            userGoalDao.findUserGoal(user)
                ?: userGoalDao.save(UserGoal(user = user))
        userGoal.updateRunningPurpose(runningPurpose.name)
        return userGoal
    }

    fun getUserGoal(user: User): UserGoal {
        return userGoalDao.getUserGoal(user)
    }
}
