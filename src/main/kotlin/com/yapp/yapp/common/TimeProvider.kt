package com.yapp.yapp.common

import java.time.OffsetDateTime
import java.time.ZoneId

object TimeProvider {
    fun now(): OffsetDateTime = OffsetDateTime.now(ZoneId.of("Asia/Seoul"))

    fun parse(timeStamp: String): OffsetDateTime {
        return OffsetDateTime.parse(timeStamp)
    }
}
