package com.yapp.yapp.user.domain

enum class RunnerType(
    val description: String,
) {
    BEGINNER("워밍업"),
    INTERMEDIATE("루틴"),
    EXPERT("챌린저"),
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
    }
}
