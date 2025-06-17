package com.yapp.yapp.running

import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findLastByRunningRecordAndIsDeletedFalse(runningRecord: RunningRecord): RunningPoint?
}
