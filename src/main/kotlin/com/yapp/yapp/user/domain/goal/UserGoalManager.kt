package com.yapp.yapp.user.domain.goal

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.User
import org.springframework.stereotype.Component

@Component
class UserGoalManager(
    private val userGoalDao: UserGoalDao,
) {
    fun saveWeeklyRunCountGoal(
        user: User,
        weeklyRunCount: Int,
        remindAlert: Boolean,
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
        time: Long,
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

    fun hasUserGoal(user: User): Boolean {
        return userGoalDao.findUserGoal(user) != null
    }

    fun findUserGoal(user: User): UserGoal? {
        return userGoalDao.findUserGoal(user)
    }

    fun calculateRecommendPace(recentRunningRecord: RunningRecord?): Pace {
        if (recentRunningRecord == null || !recentRunningRecord.isValidRecord()) {
            return Pace.createBeginnerPace()
        }
        val averagePace = recentRunningRecord.averagePace
        val expertPace = Pace.createExpertPace()
        if (averagePace.millsPerKm < expertPace.millsPerKm) {
            return averagePace
        }
        val intermediatePace = Pace.createIntermediatePace()
        if (averagePace.millsPerKm < intermediatePace.millsPerKm) {
            return Pace((expertPace.millsPerKm + averagePace.millsPerKm) / 2)
        }
        val beginnerPace = Pace.createBeginnerPace()
        if (averagePace.millsPerKm < beginnerPace.millsPerKm) {
            return Pace((intermediatePace.millsPerKm + averagePace.millsPerKm) / 2)
        }
        return beginnerPace
    }
}
