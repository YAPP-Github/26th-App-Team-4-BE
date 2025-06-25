package com.yapp.yapp.record

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class RecordDao(
    private val recordRepository: RecordRepository,
) {
    fun save(record: Record): Record {
        return recordRepository.save(record)
    }

    fun getById(id: Long): Record {
        return recordRepository.findById(id)
            .orElseThrow { throw CustomException(ErrorCode.RECORD_NOT_FOUND) }
    }
}
