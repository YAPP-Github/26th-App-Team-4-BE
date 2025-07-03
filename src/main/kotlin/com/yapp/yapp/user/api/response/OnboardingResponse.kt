package com.yapp.yapp.user.api.response

import com.yapp.yapp.user.api.request.OnboardingAnswerDto

data class OnboardingResponse(
    val userId: Long,
    val answerList: List<OnboardingAnswerDto>,
)
