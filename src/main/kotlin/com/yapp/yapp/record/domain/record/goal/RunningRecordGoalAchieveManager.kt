package com.yapp.yapp.record.domain.record.goal

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.record.RunningRecord
import com.yapp.yapp.user.domain.goal.UserGoal
import org.springframework.stereotype.Component

@Component
class RunningRecordGoalAchieveManager(
    private val runningRecordGoalAchieveDao: RunningRecordGoalAchieveDao,
) {
    fun save(runningRecord: RunningRecord): RunningRecordGoalAchieve {
        val goalAchieve = RunningRecordGoalAchieve(runningRecord = runningRecord)
        return runningRecordGoalAchieveDao.save(goalAchieve)
    }

    fun update(
        runningRecord: RunningRecord,
        userGoal: UserGoal,
    ) {
        val goalAchieve = getByRecord(runningRecord)

        if (userGoal.isPaceGoalAchieved(runningRecord)) {
            goalAchieve.achievedPaceGoal()
        }
        if (userGoal.isTimeGoalAchieved(runningRecord)) {
            goalAchieve.achievedTimeGoal()
        }
        if (userGoal.isDistanceGoalAchieved(runningRecord)) {
            goalAchieve.achievedDistanceGoal()
        }
    }

    fun findByRecord(runningRecord: RunningRecord): RunningRecordGoalAchieve? {
        return runningRecordGoalAchieveDao.findByRecord(runningRecord)
    }

    fun getByRecord(runningRecord: RunningRecord): RunningRecordGoalAchieve {
        return runningRecordGoalAchieveDao.findByRecord(runningRecord)
            ?: throw CustomException(ErrorCode.RECORD_GOAL_ARCHIVE_NO_MATCHED)
    }
}
