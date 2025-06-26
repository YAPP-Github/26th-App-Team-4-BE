package com.yapp.yapp.record.domain.point

import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findTopByRunningRecordAndIsDeletedFalseOrderByOrdDesc(runningRecord: RunningRecord): RunningPoint?

    fun findAllByRunningRecordAndIsDeletedFalseOrderByOrdAsc(runningRecord: RunningRecord): List<RunningPoint>
}
