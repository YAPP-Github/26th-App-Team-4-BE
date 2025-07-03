package com.yapp.yapp.user.domain.onbording

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.user.domain.User
import org.springframework.stereotype.Component

@Component
class OnboardingDao(
    val onboardingRepository: OnboardingRepository,
) {
    fun saveAll(onboarding: List<Onboarding>): List<Onboarding> {
        return onboardingRepository.saveAll(onboarding)
            .toList()
    }

    fun findAllByUser(user: User): List<Onboarding> {
        return onboardingRepository.findAllByUserAndIsDeletedFalse(user)
    }

    fun findAnswerByUser(
        user: User,
        questionType: OnboardingQuestionType,
    ): Onboarding {
        return onboardingRepository.findByUserAndQuestionTypeAndIsDeletedFalse(user = user, questionType = questionType)
            ?: throw CustomException(ErrorCode.ANSWER_NOT_FOUND)
    }
}
