package com.yapp.yapp.record.domain.point

import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface RunningPointRepository : CrudRepository<RunningPoint, Long> {
    fun findTopByRunningRecordAndIsDeletedFalseOrderByOrderNoDesc(runningRecord: RunningRecord): RunningPoint?

    fun findAllByRunningRecordAndIsDeletedFalseOrderByOrderNoAsc(runningRecord: RunningRecord): List<RunningPoint>

    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM RunningPoint p WHERE p.runningRecord.id IN :runningRecordIds")
    fun deleteByRunningRecordIdIn(runningRecordIds: List<Long>)
}
