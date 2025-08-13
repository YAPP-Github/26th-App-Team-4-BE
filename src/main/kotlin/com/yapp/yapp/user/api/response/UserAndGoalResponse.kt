package com.yapp.yapp.user.api.response

data class UserAndGoalResponse(
    val user: UserResponse,
    val goal: UserGoalResponse?,
)
