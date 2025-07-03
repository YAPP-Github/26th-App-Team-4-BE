package com.yapp.yapp.user.domain.onbording

import com.yapp.yapp.user.domain.User
import org.springframework.data.repository.CrudRepository

interface OnboardingRepository : CrudRepository<Onboarding, Long> {
    fun findAllByUserAndIsDeletedFalse(user: User): List<Onboarding>
}
