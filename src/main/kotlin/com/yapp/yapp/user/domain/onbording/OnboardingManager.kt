package com.yapp.yapp.user.domain.onbording

import org.springframework.stereotype.Component

@Component
class OnboardingManager(
    val onboardingDao: OnboardingDao,
) {
    fun saveAll(onboarding: List<Onboarding>): List<Onboarding> {
        return onboardingDao.saveAll(onboarding)
    }
}
