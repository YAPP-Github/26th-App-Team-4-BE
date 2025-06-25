package com.yapp.yapp.running.domain.point

import com.yapp.yapp.running.domain.record.RunningRecord
import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findTopByRunningRecordAndIsDeletedFalseOrderByOrdDesc(runningRecord: RunningRecord): RunningPoint?

    fun findAllByRunningRecordAndIsDeletedFalseOrderByOrdAsc(runningRecord: RunningRecord): List<RunningPoint>
}
