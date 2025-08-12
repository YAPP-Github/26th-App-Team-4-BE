package com.yapp.yapp.user.domain

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.Pace

enum class RunnerType(
    val description: String,
    val recommendPace: Pace,
) {
    BEGINNER("워밍업", Pace(TimeProvider.minuteToMills(10))),
    INTERMEDIATE("루틴", Pace(TimeProvider.minuteToMills(8))),
    EXPERT("챌린저", Pace(TimeProvider.minuteToMills(6))),
    ;

    companion object {
        fun calculateRunnerType(
            noCount: Int,
            yesNoCount: Int,
        ): RunnerType {
            if (yesNoCount == 0) {
                return INTERMEDIATE
            }
            val ratio = noCount.toDouble() / yesNoCount
            return when (ratio) {
                in 0.0..<0.4 -> EXPERT
                in 0.4..<0.7 -> INTERMEDIATE
                else -> BEGINNER
            }
        }

        fun getByName(name: String): RunnerType {
            return entries.find { it.name == name }
                ?: throw CustomException(ErrorCode.RUNNER_TYPE_NOT_FOUND)
        }
    }
}
