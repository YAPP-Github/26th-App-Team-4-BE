package com.yapp.yapp.record.domain.point

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.record.RunningRecord
import org.springframework.stereotype.Component

@Component
class RunningPointDao(
    private val runningPointRepository: RunningPointRepository,
    private val runningPointJdbcBatchRepository: RunningPointJdbcBatchRepository,
) {
    fun save(runningPoint: RunningPoint): RunningPoint {
        return runningPointRepository.save(runningPoint)
    }

    fun saveAll(
        runningRecord: RunningRecord,
        runningPoints: List<RunningPoint>,
    ) {
        return runningPointJdbcBatchRepository.batchInsert(
            recordId = runningRecord.id,
            points = runningPoints,
        )
    }

    fun get(id: Long): RunningPoint {
        return runningPointRepository.findById(id)
            .orElseThrow { throw CustomException(ErrorCode.POINT_NOT_FOUND) }
    }

    fun getPrePointByRunningRecord(runningRecord: RunningRecord): RunningPoint {
        return runningPointRepository.findTopByRunningRecordAndIsDeletedFalseOrderByOrderNoDesc(runningRecord)
            ?: throw CustomException(ErrorCode.POINT_NOT_FOUND)
    }

    fun getAllPointByRunningRecord(runningRecord: RunningRecord): List<RunningPoint> {
        return runningPointRepository.findAllByRunningRecordAndIsDeletedFalseOrderByOrderNoAsc(runningRecord)
    }

    fun deleteByRunningRecordIdIn(runningRecordIds: List<Long>) {
        runningPointRepository.deleteByRunningRecordIdIn(runningRecordIds)
    }
}
