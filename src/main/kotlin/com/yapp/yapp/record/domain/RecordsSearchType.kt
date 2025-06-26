package com.yapp.yapp.record.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class RecordsSearchType {
    ALL,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    ;

    companion object {
        fun getBy(type: String): RecordsSearchType {
            return values().find { it.name == type }
                ?: throw CustomException(ErrorCode.SEARCH_TYPE_NO_MATCHED)
        }
    }
}
