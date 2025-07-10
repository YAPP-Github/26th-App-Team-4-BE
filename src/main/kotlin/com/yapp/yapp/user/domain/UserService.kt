package com.yapp.yapp.user.domain

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.user.api.request.DistanceGoalSaveRequest
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.request.PaceGoalSaveRequest
import com.yapp.yapp.user.api.request.TimeGoalSaveRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalSaveRequest
import com.yapp.yapp.user.api.response.AnswerResponse
import com.yapp.yapp.user.api.response.UserGoalResponse
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.onboarding.Onboarding
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
    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userManager.getActiveUser(id)
        return UserResponse(user.id, user.nickname, user.email, user.provider)
    }

    @Transactional
    fun delete(id: Long) {
        val findUser = userManager.getActiveUser(id)
        findUser.isDeleted = true
    }

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
        onboardingManager.saveAll(onboardings)
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
    fun saveGoal(
        userId: Long,
        request: WeeklyRunCountGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        val saveWeeklyRunCountGoal = userGoalManager.saveWeeklyRunCountGoal(user = user, weeklyRunCount = request.count)
        return saveWeeklyRunCountGoal
    }

    @Transactional
    fun saveGoal(
        userId: Long,
        request: PaceGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.savePaceGoal(user = user, pace = Pace(request.pace))
    }

    @Transactional
    fun saveGoal(
        userId: Long,
        request: DistanceGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.saveDistanceGoal(user = user, distanceMeter = request.distanceMeter)
    }

    @Transactional
    fun saveGoal(
        userId: Long,
        request: TimeGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.saveTimeGoal(user = user, time = Duration.parse(request.time))
    }

    @Transactional(readOnly = true)
    fun getGoal(userId: Long): UserGoalResponse {
        val user = userManager.getActiveUser(userId)
        val userGoal = userGoalManager.getUserGoal(user)
        return UserGoalResponse(userGoal)
    }

    @Transactional
    fun updateGoal(
        userId: Long,
        request: WeeklyRunCountGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.saveWeeklyRunCountGoal(user = user, weeklyRunCount = request.count)
    }

    @Transactional
    fun updateGoal(
        userId: Long,
        request: PaceGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.savePaceGoal(user = user, pace = Pace(request.pace))
    }

    @Transactional
    fun updateGoal(
        userId: Long,
        request: DistanceGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.saveDistanceGoal(user = user, distanceMeter = request.distanceMeter)
    }

    @Transactional
    fun updateGoal(
        userId: Long,
        request: TimeGoalSaveRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return userGoalManager.saveTimeGoal(user = user, time = Duration.parse(request.time))
    }
}
