package com.yapp.yapp.support.fixture

import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.request.PaceGoalRequest
import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType

object RequestFixture {
    fun runningStartRequest(
        lat: Double = 37.5665,
        lon: Double = 126.9780,
        timeStamp: String = "2025-06-17T16:12:00+09:00",
    ) = RunningStartRequest(
        lat = lat,
        lon = lon,
        timeStamp = timeStamp,
    )

    fun runningUpdateRequest(
        recordId: Long = 1L,
        lat: Double = 37.5665,
        lon: Double = 126.9780,
        heartRate: Int = 142,
        totalRunningTime: Long = (1 * 60 + 2) * 1000 + 12,
        timeStamp: String = "2025-06-17T16:12:00+09:00",
    ) = RunningUpdateRequest(
        lat = lat,
        lon = lon,
        heartRate = heartRate,
        totalRunningTime = totalRunningTime,
        timeStamp = timeStamp,
    )

    fun onboardingRequest(
        answers: List<OnboardingAnswerDto> =
            listOf(
                OnboardingAnswerDto(OnboardingQuestionType.EXPLOSIVE_STRENGTH, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.AGILITY, OnboardingAnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.COORDINATION, OnboardingAnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.BALANCE, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.EXERCISE_EXPERIENCE, OnboardingAnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_EXPERIENCE, OnboardingAnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_ENDURANCE, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.PACE_AWARENESS, OnboardingAnswerLabel.B),
            ),
    ): OnboardingRequest {
        return OnboardingRequest(answers)
    }

    fun paceGoalRequest(pace: Long = (7 * 60 + 30) * 1000L): PaceGoalRequest {
        return PaceGoalRequest(pace)
    }
}
