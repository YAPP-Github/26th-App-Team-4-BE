package com.yapp.yapp.record.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class RecordStatus {
    READY,
    IN_PROGRESS,
    PAUSE,
    DONE,
    ;

    companion object {
        fun getBy(type: String): RecordStatus {
            return RecordStatus.entries.find { it.name == type }
                ?: throw CustomException(ErrorCode.RECORD_TYPE_NO_MATCHED)
        }
    }
}
