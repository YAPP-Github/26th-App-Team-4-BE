package com.yapp.yapp.user.domain

enum class RunnerType {
    BEGINNER,
    INTERMEDIATE,
    EXPERT,
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
