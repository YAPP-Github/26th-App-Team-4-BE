package com.yapp.yapp.running.domain.record

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class RunningRecordDao(
    private val runningRecordRepository: RunningRecordRepository,
) {
    fun save(runningRecord: RunningRecord): RunningRecord {
        return runningRecordRepository.save(runningRecord)
    }

    fun getById(id: Long): RunningRecord {
        return runningRecordRepository.findById(id)
            .orElseThrow { throw CustomException(ErrorCode.RECORD_NOT_FOUND) }
    }
}
