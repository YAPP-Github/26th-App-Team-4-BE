package com.yapp.yapp.term.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class TermType() {
    SERVICE,
    PRIVATE_POLICY,
    LOCATION,
    WITHDRAW,
    ;

    companion object {
        fun from(name: String): TermType =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: throw CustomException(ErrorCode.INVALID_REQUEST)
    }
}
