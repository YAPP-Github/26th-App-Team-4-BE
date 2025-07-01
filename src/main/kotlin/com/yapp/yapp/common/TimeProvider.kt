package com.yapp.yapp.common

import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

object TimeProvider {
    val zone = ZoneId.of("Asia/Seoul")

    fun now(): OffsetDateTime {
        return OffsetDateTime.now(zone)
    }

    fun year(year: Int): OffsetDateTime = now().withYear(year)

    fun month(month: Int): OffsetDateTime = now().withMonth(month)

    fun day(day: Int): OffsetDateTime = now().withDayOfMonth(day)

    fun hour(hour: Int): OffsetDateTime = now().withHour(hour)

    fun minute(minute: Int): OffsetDateTime = now().withMinute(minute)

    fun parse(timeStamp: String): OffsetDateTime {
        return OffsetDateTime.parse(timeStamp)
    }

    fun from(localDateTime: LocalDateTime): OffsetDateTime {
        return localDateTime.atZone(zone).toOffsetDateTime()
    }

    fun OffsetDateTime.toStartOfDay(): OffsetDateTime = this.withHour(0).withMinute(0).withSecond(0).withNano(0)

    fun List<Duration>.average(): Duration =
        if (isEmpty()) {
            Duration.ZERO
        } else {
            fold(Duration.ZERO) { acc, duration -> acc.plus(duration) }.dividedBy(size.toLong())
        }
}
