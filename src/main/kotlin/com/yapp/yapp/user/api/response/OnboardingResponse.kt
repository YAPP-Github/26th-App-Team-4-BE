package com.yapp.yapp.user.api.response

data class OnboardingResponse(
    val userId: Long,
    val answerList: List<AnswerResponse>,
)
