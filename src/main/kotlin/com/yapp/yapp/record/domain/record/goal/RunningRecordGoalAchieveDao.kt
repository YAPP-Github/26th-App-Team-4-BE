package com.yapp.yapp.record.domain.record.goal

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.stereotype.Component

@Component
class RunningRecordGoalAchieveDao(
    private val runningRecordGoalAchieveRepository: RunningRecordGoalAchieveRepository,
) {
    fun save(runningRecordGoalAchieve: RunningRecordGoalAchieve): RunningRecordGoalAchieve {
        return runningRecordGoalAchieveRepository.save(runningRecordGoalAchieve)
    }

    fun getByRecord(runningRecord: RunningRecord): RunningRecordGoalAchieve {
        return runningRecordGoalAchieveRepository.findByRunningRecord(runningRecord)
            ?: throw CustomException(ErrorCode.RECORD_GOAL_ARCHIVE_NO_MATCHED)
    }
}
