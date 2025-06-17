package com.yapp.yapp.running

import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findLastByRunningRecord(runningRecord: RunningRecord): RunningPoint?
}
