package com.yapp.yapp.running

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class RunningPointDao(
    private val runningPointRepository: RunningPointRepository,
) {
    fun save(runningPoint: RunningPoint): RunningPoint {
        return runningPointRepository.save(runningPoint)
    }

    fun get(id: Long): RunningPoint {
        return runningPointRepository.findById(id)
            .orElseThrow { throw CustomException(ErrorCode.RUNNING_POINT_NOT_FOUND) }
    }

    fun getPrePointByRunningRecord(runningRecord: RunningRecord): RunningPoint {
        return runningPointRepository.findTopByRunningRecordAndIsDeletedFalseOrderByOrdDesc(runningRecord)
            ?: throw CustomException(ErrorCode.RUNNING_POINT_NOT_FOUND)
    }

    fun getAllPointByRunningRecord(runningRecord: RunningRecord): List<RunningPoint> {
        return runningPointRepository.findAllByRunningRecordAndIsDeletedFalseOrderByOrdAsc(runningRecord)
    }
}
