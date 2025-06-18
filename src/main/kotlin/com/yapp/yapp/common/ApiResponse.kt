package com.yapp.yapp.common

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.OffsetDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val code: String,
    val result: T? = null,
    val timeStamp: OffsetDateTime? = null,
    val message: String? = null,
) {
    companion object {
        fun <T> success(result: T): ApiResponse<T> = ApiResponse(code = "SUCCESS", result = result, timeStamp = TimeProvider.now())

        fun error(
            code: String,
            message: String,
        ): ApiResponse<Unit> =
            ApiResponse(
                code = code,
                timeStamp = TimeProvider.now(),
                message = message,
            )
    }
}
