package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType

data class AnswerResponse(
    val questionType: OnboardingQuestionType,
    val answer: OnboardingAnswerLabel,
)
