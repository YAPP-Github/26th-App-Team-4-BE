package com.yapp.yapp.user.domain.onboarding

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

    fun getQuestion(
        user: User,
        questionType: OnboardingQuestionType,
    ): Onboarding = onboardingDao.getAnswerByUser(user, questionType)

    fun updateQuestion(
        user: User,
        questionType: OnboardingQuestionType,
        answer: OnboardAnswerLabel,
    ): Onboarding {
        return onboardingDao.updateQuestion(user, questionType, answer)
    }
}
