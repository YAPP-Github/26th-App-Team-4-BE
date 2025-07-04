package com.yapp.yapp.user.domain.onboarding

import com.yapp.yapp.user.domain.User
import org.springframework.data.repository.CrudRepository

interface OnboardingRepository : CrudRepository<Onboarding, Long> {
    fun findAllByUser(user: User): List<Onboarding>

    fun findByUserAndQuestionType(
        user: User,
        questionType: OnboardingQuestionType,
    ): Onboarding?
}
