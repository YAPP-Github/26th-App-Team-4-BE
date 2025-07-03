package com.yapp.yapp.user.domain.onboarding

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

    fun getAnswerByUser(
        user: User,
        questionType: OnboardingQuestionType,
    ): Onboarding {
        return onboardingRepository.findByUserAndQuestionTypeAndIsDeletedFalse(user = user, questionType = questionType)
            ?: throw CustomException(ErrorCode.ANSWER_NOT_FOUND)
    }

    fun updateQuestion(
        user: User,
        questionType: OnboardingQuestionType,
        answer: OnboardAnswerLabel,
    ): Onboarding {
        val onboarding =
            onboardingRepository.findByUserAndQuestionTypeAndIsDeletedFalse(user = user, questionType = questionType)
                ?: onboardingRepository.save(Onboarding(user = user, questionType = questionType, answer = answer))
        onboarding.answer = answer
        return onboarding
    }
}
