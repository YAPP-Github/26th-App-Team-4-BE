package com.yapp.yapp.common.token.jwt

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class TokenType {
    ACCESS,
    REFRESH,
    ;

    companion object {
        fun from(name: String?): TokenType =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: throw CustomException(ErrorCode.TOKEN_INVALID)
    }
}
