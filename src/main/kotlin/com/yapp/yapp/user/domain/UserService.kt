package com.yapp.yapp.user.domain

import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.response.UserResponse
import com.yapp.yapp.user.domain.onbording.Onboarding
import com.yapp.yapp.user.domain.onbording.OnboardingManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userManager: UserManager,
    private val onboardingManager: OnboardingManager,
) {
    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userManager.getActiveUser(id)
        return UserResponse(user.id, user.name, user.email, user.profileImage, user.provider)
    }

    @Transactional
    fun delete(id: Long) {
        val findUser = userManager.getActiveUser(id)
        findUser.isDeleted = true
    }

    @Transactional
    fun saveOnboarding(
        id: Long,
        request: OnboardingRequest,
    ) {
        val user = userManager.getActiveUser(id)
        val onboardings =
            request.answers.map {
                Onboarding(
                    user = user,
                    QuestionType = it.questionType,
                    answer = it.answer,
                )
            }
        onboardingManager.saveAll(onboardings)
    }

    @Transactional(readOnly = true)
    fun getOnboardings(id: Long): List<OnboardingAnswerDto> {
        val user = userManager.getActiveUser(id)
        return onboardingManager.onboardingDao.findAllByUser(user)
            .map { onboarding ->
                OnboardingAnswerDto(
                    questionType = onboarding.QuestionType,
                    answer = onboarding.answer,
                )
            }
    }
}
