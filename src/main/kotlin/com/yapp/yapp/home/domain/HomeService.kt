package com.yapp.yapp.home.domain

import com.yapp.yapp.home.api.response.HomeResponse
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.user.domain.UserManager
import com.yapp.yapp.user.domain.goal.UserGoalManager
import com.yapp.yapp.user.domain.onboarding.OnboardingManager
import org.springframework.stereotype.Service

@Service
class HomeService(
    private val userManager: UserManager,
    private val onboardingManager: OnboardingManager,
    private val recordManager: RunningRecordManager,
    private val userGoalManager: UserGoalManager,
) {
    fun getHomeScreenData(id: Long): HomeResponse {
        val user = userManager.getActiveUser(id)
        val userGoal = userGoalManager.getUserGoal(user)
//        val runningRecord = recordManager.findRecentRunningRecord(user)
//        recordManager
//            .HomeResponse()
        TODO()
    }
}
