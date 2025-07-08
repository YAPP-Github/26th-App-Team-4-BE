package com.yapp.yapp.record.domain

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.TimeProvider.toStartOfDay
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import java.time.DayOfWeek
import java.time.OffsetDateTime

enum class RecordsSearchType {
    ALL {
        override fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime = targetDate.toStartOfDay().minusYears(20L)

        override fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime = targetDate.toStartOfDay().plusYears(20L)
    },
    DAILY {
        override fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime = TimeProvider.from(targetDate.toLocalDate().atStartOfDay())

        override fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime =
            targetDate.withHour(
                0,
            ).plusDays(1).minusSeconds(1)
    },
    WEEKLY {
        override fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime = targetDate.toStartOfDay().with(DayOfWeek.MONDAY)

        override fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime =
            targetDate.toStartOfDay().with(
                DayOfWeek.SUNDAY,
            )
    },
    MONTHLY {
        override fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime = targetDate.toStartOfDay().withDayOfMonth(1)

        override fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime =
            targetDate.toStartOfDay().withDayOfMonth(targetDate.toLocalDate().lengthOfMonth())
    },
    YEARLY {
        override fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime = targetDate.toStartOfDay().withDayOfYear(1)

        override fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime =
            targetDate.toStartOfDay().withDayOfYear(targetDate.toLocalDate().lengthOfYear())
    }, ;

    abstract fun getStartDate(targetDate: OffsetDateTime): OffsetDateTime

    abstract fun getEndDate(targetDate: OffsetDateTime): OffsetDateTime

    companion object {
        fun getBy(type: String): RecordsSearchType {
            return values().find { it.name == type }
                ?: throw CustomException(ErrorCode.SEARCH_TYPE_NO_MATCHED)
        }
    }
}
