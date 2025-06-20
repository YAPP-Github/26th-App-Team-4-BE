package com.yapp.yapp.running

import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findTopByRunningRecordAndIsDeletedFalseOrderByOrdDesc(runningRecord: RunningRecord): RunningPoint?

    fun findAllByRunningRecordAndIsDeletedFalseOrderByOrdAsc(runningRecord: RunningRecord): List<RunningPoint>
}
