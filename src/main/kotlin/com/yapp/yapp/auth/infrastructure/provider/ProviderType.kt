package com.yapp.yapp.auth.infrastructure.provider

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode

enum class ProviderType {
    KAKAO,
    APPLE,
    ;

    companion object {
        fun from(providerName: String): ProviderType {
            return entries.find { it.name.equals(providerName, ignoreCase = true) }
                ?: throw CustomException(ErrorCode.UNSUPPORTED_PROVIDER_TYPE)
        }
    }
}
