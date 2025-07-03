package com.yapp.yapp.user.domain.onbording

import com.yapp.yapp.user.domain.User
import org.springframework.stereotype.Component

@Component
class OnboardingManager(
    val onboardingDao: OnboardingDao,
) {
    fun saveAll(onboarding: List<Onboarding>): List<Onboarding> {
        return onboardingDao.saveAll(onboarding)
    }

    fun getAll(user: User): List<Onboarding> = onboardingDao.findAllByUser(user)

    fun getGoal(user: User): Onboarding = onboardingDao.findAnswerByUser(user, OnboardingQuestionType.GOAL)
}
