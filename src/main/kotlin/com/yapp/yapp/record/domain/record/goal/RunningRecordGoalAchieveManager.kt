package com.yapp.yapp.record.domain.record.goal

import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.goal.UserGoal
import org.springframework.stereotype.Component

@Component
class RunningRecordGoalAchieveManager(
    private val runningRecordGoalAchieveDao: RunningRecordGoalAchieveDao,
) {
    fun save(
        userGoal: UserGoal,
        runningRecord: RunningRecord,
    ): RunningRecordGoalAchieve {
        val goalAchieve =
            RunningRecordGoalAchieve(
                runningRecord = runningRecord,
                isPaceGoalAchieved = userGoal.isPaceGoalAchieved(runningRecord),
                isTimeGoalAchieved = userGoal.isTimeGoalAchieved(runningRecord),
                isDistanceGoalAchieved = userGoal.isDistanceGoalAchieved(runningRecord),
            )
        return runningRecordGoalAchieveDao.save(goalAchieve)
    }

    fun getByRecord(runningRecord: RunningRecord): RunningRecordGoalAchieve {
        return runningRecordGoalAchieveDao.getByRecord(runningRecord)
    }
}
