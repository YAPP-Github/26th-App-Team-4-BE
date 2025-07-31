package com.yapp.yapp.record.domain.point

import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findTopByRunningRecordAndIsDeletedFalseOrderByOrderNoDesc(runningRecord: RunningRecord): RunningPoint?

    fun findAllByRunningRecordAndIsDeletedFalseOrderByOrderNoAsc(runningRecord: RunningRecord): List<RunningPoint>

    fun deleteByRunningRecordIn(recordIds: List<RunningRecord>)
}
