package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.domain.onbording.AnswerLabel
import com.yapp.yapp.user.domain.onbording.OnboardingQuestionType

data class AnswerResponse(
    val questionType: OnboardingQuestionType,
    val answer: AnswerLabel,
)
