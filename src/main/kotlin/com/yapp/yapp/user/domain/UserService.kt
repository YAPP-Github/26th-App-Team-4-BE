package com.yapp.yapp.user.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.api.request.DistanceGoalRequest
import com.yapp.yapp.user.api.request.GoalRequest
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.request.PaceGoalRequest
import com.yapp.yapp.user.api.request.RunningPurposeRequest
import com.yapp.yapp.user.api.request.TimeGoalRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalRequest
import com.yapp.yapp.user.api.response.AnswerResponse
import com.yapp.yapp.user.api.response.RunnerTypeResponse
import com.yapp.yapp.user.api.response.UserGoalResponse
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.goal.UserGoal
import com.yapp.yapp.user.domain.goal.UserGoalManager
import com.yapp.yapp.user.domain.onboarding.Onboarding
import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class UserService(
    private val userManager: UserManager,
    private val onboardingManager: OnboardingManager,
    private val userGoalManager: UserGoalManager,
) {
    @Transactional
    fun saveOnboarding(
        id: Long,
        request: OnboardingRequest,
    ) {
        val user = userManager.getActiveUser(id)
        val onboardings =
            request.answers.map {
                Onboarding(
                    user = user,
                    questionType = it.questionType,
                    answer = it.answer,
                )
            }
        val yseNoCount = onboardings.count { it.answer != OnboardingAnswerLabel.B }
        val noCount = onboardings.count { it.answer == OnboardingAnswerLabel.C }
        val runnerType = RunnerType.calculateRunnerType(noCount, yseNoCount)

        onboardingManager.saveAll(onboardings)
        userManager.updateRunnerType(user = user, runnerType = runnerType)
    }

    @Transactional(readOnly = true)
    fun getOnboardings(id: Long): List<AnswerResponse> {
        val user = userManager.getActiveUser(id)
        return onboardingManager.getAll(user)
            .map { onboarding ->
                AnswerResponse(
                    questionType = onboarding.questionType,
                    answer = onboarding.answer,
                )
            }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userManager.getActiveUser(id)
        return UserResponse(user.id, user.nickname, user.email, user.provider)
    }

    @Transactional(readOnly = true)
    fun getGoal(userId: Long): UserGoalResponse {
        val user = userManager.getActiveUser(userId)
        val userGoal = userGoalManager.getUserGoal(user)
        return UserGoalResponse(userGoal)
    }

    @Transactional(readOnly = true)
    fun getRunnerType(userId: Long): RunnerTypeResponse {
        val user = userManager.getActiveUser(userId)
        val runnerType: RunnerType =
            user.runnerType
                ?: throw CustomException(ErrorCode.RUNNER_TYPE_NOT_FOUND)
        return RunnerTypeResponse(userId = user.id, runnerType = runnerType)
    }

    @Transactional
    fun updateOnboardings(
        id: Long,
        request: OnboardingRequest,
    ): List<AnswerResponse> {
        val user = userManager.getActiveUser(id)
        request.answers.forEach {
            onboardingManager.updateQuestion(user, it.questionType, it.answer)
        }
        return onboardingManager.getAll(user).map { onboarding ->
            AnswerResponse(
                questionType = onboarding.questionType,
                answer = onboarding.answer,
            )
        }
    }

    @Transactional
    fun upsertGoal(
        userId: Long,
        request: GoalRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return when (request) {
            is WeeklyRunCountGoalRequest -> userGoalManager.saveWeeklyRunCountGoal(user, request.count)
            is PaceGoalRequest -> userGoalManager.savePaceGoal(user, Pace(request.pace))
            is DistanceGoalRequest -> userGoalManager.saveDistanceGoal(user, request.distanceMeter)
            is TimeGoalRequest -> userGoalManager.saveTimeGoal(user, Duration.parse(request.time))
            is RunningPurposeRequest -> userGoalManager.saveRunningPurpose(user, request.runningPurpose)
        }
    }

    @Transactional
    fun delete(id: Long) {
        val findUser = userManager.getActiveUser(id)
        findUser.isDeleted = true
    }
}
