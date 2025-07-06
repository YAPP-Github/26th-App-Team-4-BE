package com.yapp.yapp.home.api

import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel

data class HomeResponse(
    val user: UserResponse,
    val totalRecord: TotalRecordResponse,
) {
    data class UserResponse(
        val id: Long,
        val nickname: String,
        val goal: OnboardingAnswerLabel,
        // TODO 러닝 목표
    )

    data class TotalRecordResponse(
        val totalDistance: Double,
        val thisWeekRunningCount: Int,
        val thisWeekFullfeelCount: Int,
    )
}
