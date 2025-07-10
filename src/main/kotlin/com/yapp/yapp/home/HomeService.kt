package com.yapp.yapp.home

import com.yapp.yapp.user.domain.UserManager
import com.yapp.yapp.user.domain.onboarding.OnboardingManager
import org.springframework.stereotype.Service

@Service
class HomeService(
    private val userManager: UserManager,
    private val onboardingManager: OnboardingManager,
) {
    fun getHomeScreenData(id: Long) {
        val user = userManager.getActiveUser(id)
        TODO()
    }
}
