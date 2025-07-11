package com.yapp.yapp.user.domain

enum class RunnerType(
    val description: String,
) {
    BEGINNER("워밍업"),
    INTERMEDIATE("루틴"),
    EXPERT("챌린저"),
    ;

    companion object {
        fun calculateRunnerType(noCount: Int): RunnerType {
            return when (noCount) {
                in 0..3 -> EXPERT
                in 4..6 -> INTERMEDIATE
                else -> BEGINNER
            }
        }
    }
}
