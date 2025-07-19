package com.yapp.yapp.home.domain

import com.yapp.yapp.home.api.response.HomeResponse
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.record.domain.record.RunningRecordManager
import com.yapp.yapp.user.domain.UserManager
import com.yapp.yapp.user.domain.goal.UserGoalManager
import com.yapp.yapp.user.domain.onboarding.OnboardingManager
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class HomeService(
    private val userManager: UserManager,
    private val onboardingManager: OnboardingManager,
    private val recordManager: RunningRecordManager,
    private val userGoalManager: UserGoalManager,
) {
    fun getHomeScreenData(
        userId: Long,
        targetDate: OffsetDateTime,
    ): HomeResponse {
        val user = userManager.getActiveUser(userId)
        val userGoal = userGoalManager.getUserGoal(user)
        val recentRunningRecord = recordManager.findRecentRunningRecord(user)
        val totalRecord =
            recordManager.getTotalRecord(
                user = user,
                searchType = RecordsSearchType.ALL,
                targetDate = targetDate,
            )
        val thisWeekRunningCount = recordManager.getThisWeekRunningCount(user)

        return HomeResponse(
            user = user,
            userGoal = userGoal,
            totalRecord = totalRecord,
            recentRecord = recentRunningRecord,
            thisWeekRunningCount = thisWeekRunningCount,
        )
    }
}
