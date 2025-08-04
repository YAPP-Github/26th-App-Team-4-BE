package com.yapp.yapp.record.domain.record.goal

import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.data.repository.CrudRepository

interface RunningRecordGoalAchieveRepository : CrudRepository<RunningRecordGoalAchieve, Long> {
    fun findByRunningRecord(runningRecord: RunningRecord): RunningRecordGoalAchieve?
}
