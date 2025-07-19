package com.yapp.yapp.user.api.request

import com.yapp.yapp.user.domain.goal.RunningPurposeAnswerLabel

sealed interface GoalRequest

data class WeeklyRunCountGoalRequest(val count: Int) : GoalRequest

data class PaceGoalRequest(val pace: Long) : GoalRequest

data class DistanceGoalRequest(val distanceMeter: Double) : GoalRequest

data class TimeGoalRequest(val time: Long) : GoalRequest

data class RunningPurposeRequest(val runningPurpose: RunningPurposeAnswerLabel) : GoalRequest
