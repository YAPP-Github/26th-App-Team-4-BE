package com.yapp.yapp.common

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

object TimeProvider {
    val zone = ZoneId.of("Asia/Seoul")

    fun now(): OffsetDateTime {
        return OffsetDateTime.now(zone)
    }

    fun parse(timeStamp: String): OffsetDateTime {
        return OffsetDateTime.parse(timeStamp)
    }

    fun from(localDateTime: LocalDateTime): OffsetDateTime {
        return localDateTime.atZone(zone).toOffsetDateTime()
    }
}
