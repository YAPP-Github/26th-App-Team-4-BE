package com.yapp.yapp.user.api.request

import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType

data class OnboardingAnswerDto(
    val questionType: OnboardingQuestionType,
    val answer: OnboardingAnswerLabel,
)
