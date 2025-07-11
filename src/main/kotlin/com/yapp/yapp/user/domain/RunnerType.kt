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
            yseNoCount: Int,
        ): RunnerType {
            val ratio = noCount.toDouble() / yseNoCount
            return when (ratio) {
                in 0.0..0.3 -> EXPERT
                in 0.4..0.6 -> INTERMEDIATE
                else -> BEGINNER
            }
        }
    }
}
