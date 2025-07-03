package com.yapp.yapp.support.fixture

import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.running.api.request.RunningUpdateRequest
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.domain.onboarding.AnswerLabel
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
        totalRunningTime: String = "PT1M2.12S",
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
                OnboardingAnswerDto(OnboardingQuestionType.EXPLOSIVE_STRENGTH, AnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.AGILITY, AnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.COORDINATION, AnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.BALANCE, AnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.EXERCISE_EXPERIENCE, AnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_EXPERIENCE, AnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_ENDURANCE, AnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.PACE_AWARENESS, AnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.GOAL, AnswerLabel.B),
            ),
    ): OnboardingRequest {
        return OnboardingRequest(answers)
    }
}
