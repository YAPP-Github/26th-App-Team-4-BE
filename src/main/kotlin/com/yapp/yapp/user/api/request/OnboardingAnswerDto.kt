package com.yapp.yapp.user.api.request

import com.yapp.yapp.user.domain.onbording.AnswerLabel
import com.yapp.yapp.user.domain.onbording.OnboardingQuestionType

data class OnboardingAnswerDto(
    val questionType: OnboardingQuestionType,
    val answer: AnswerLabel,
)
