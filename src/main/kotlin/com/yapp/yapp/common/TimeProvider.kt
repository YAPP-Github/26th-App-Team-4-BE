package com.yapp.yapp.common

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

object TimeProvider {
    fun now(): OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)

    fun parseByLong(dateTimeLon: Long): OffsetDateTime = Instant.ofEpochMilli(dateTimeLon).atOffset(ZoneOffset.UTC)
}
