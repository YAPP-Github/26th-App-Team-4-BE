package com.yapp.yapp.user.domain

import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.user.api.request.AudioCoachingUpdateRequest
import com.yapp.yapp.user.api.request.AudioFeedbackUpdateRequest
import com.yapp.yapp.user.api.request.DistanceGoalRequest
import com.yapp.yapp.user.api.request.GoalRequest
import com.yapp.yapp.user.api.request.PaceGoalRequest
import com.yapp.yapp.user.api.request.RemindAlertUpdateRequest
import com.yapp.yapp.user.api.request.RunningPurposeRequest
import com.yapp.yapp.user.api.request.SettingUpdateRequest
import com.yapp.yapp.user.api.request.TimeGoalRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalRequest
import com.yapp.yapp.user.api.request.WithdrawRequest
import com.yapp.yapp.user.api.response.AlertSettingResponse
import com.yapp.yapp.user.api.response.AudioCoachingUpdateResponse
import com.yapp.yapp.user.api.response.AudioFeedbackUpdateResponse
import com.yapp.yapp.user.api.response.RemindAlertUpdateResponse
import com.yapp.yapp.user.api.response.RunningSettingResponse
import com.yapp.yapp.user.api.response.SettingUpdateResponse
import com.yapp.yapp.user.api.response.UserAndGoalResponse
import com.yapp.yapp.user.api.response.UserGoalResponse
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.goal.UserGoal
import com.yapp.yapp.user.domain.goal.UserGoalManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userManager: UserManager,
    private val userGoalManager: UserGoalManager,
    private val recordManager: RunningRecordManager,
    private val deletedUserManager: DeletedUserManager,
) {
    @Transactional(readOnly = true)
    fun getUserAndGoalByUserId(userId: Long): UserAndGoalResponse {
        val user = userManager.getActiveUser(userId)
        val userResponse = UserResponse(user)
        val userGoal = userGoalManager.findUserGoal(user)
        val userGoalResponse = userGoal?.let { UserGoalResponse(it) }
        return UserAndGoalResponse(user = userResponse, goal = userGoalResponse)
    }

    @Transactional(readOnly = true)
    fun getGoal(userId: Long): UserGoalResponse {
        val user = userManager.getActiveUser(userId)
        val userGoal = userGoalManager.getUserGoal(user)
        return UserGoalResponse(userGoal)
    }

    @Transactional(readOnly = true)
    fun getRecommendPace(userId: Long): Pace {
        val user = userManager.getActiveUser(userId)
        val recentRunningRecord = recordManager.findRecentRunningRecord(user)

        return userGoalManager.calculateRecommendPace(
            recentRunningRecord = recentRunningRecord,
        )
    }

    @Transactional(readOnly = true)
    fun getRunningSetting(userId: Long): RunningSettingResponse {
        val user = userManager.getActiveUser(userId)
        return RunningSettingResponse(
            audioCoaching = user.audioCoaching,
            audioFeedback = user.audioFeedback,
        )
    }

    @Transactional(readOnly = true)
    fun getAlertSetting(userId: Long): AlertSettingResponse {
        val user = userManager.getActiveUser(userId)
        return AlertSettingResponse(
            remindAlert = user.remindAlert,
        )
    }

    @Transactional
    fun updateSetting(
        userId: Long,
        request: SettingUpdateRequest,
    ): SettingUpdateResponse {
        val user = userManager.getActiveUser(userId)
        return when (request) {
            is AudioCoachingUpdateRequest -> {
                user.updateAudioCoaching(request.audioCoaching)
                AudioCoachingUpdateResponse(user.audioCoaching)
            }

            is AudioFeedbackUpdateRequest -> {
                user.updateAudioFeedback(request.audioFeedback)
                AudioFeedbackUpdateResponse(user.audioFeedback)
            }

            is RemindAlertUpdateRequest -> {
                user.updateRemindAlert(request.remindAlert)
                RemindAlertUpdateResponse(user.remindAlert)
            }
        }
    }

    @Transactional
    fun upsertGoal(
        userId: Long,
        request: GoalRequest,
    ): UserGoal {
        val user = userManager.getActiveUser(userId)
        return when (request) {
            is WeeklyRunCountGoalRequest -> {
                user.updateRemindAlert(request.remindAlert)
                userGoalManager.saveWeeklyRunCountGoal(user = user, weeklyRunCount = request.count, remindAlert = request.remindAlert)
            }

            is PaceGoalRequest -> userGoalManager.savePaceGoal(user, Pace(request.pace))
            is DistanceGoalRequest -> userGoalManager.saveDistanceGoal(user, request.distanceMeter)
            is TimeGoalRequest -> userGoalManager.saveTimeGoal(user, request.time)
            is RunningPurposeRequest -> userGoalManager.saveRunningPurpose(user, request.runningPurpose)
        }
    }

    @Transactional
    fun delete(
        userId: Long,
        withdrawRequest: WithdrawRequest,
    ) {
        userManager.delete(userId, withdrawRequest.reason)
    }

    @Transactional
    fun cleanup() {
        deletedUserManager.cleanup()
    }
}
