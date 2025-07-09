package com.yapp.yapp.user.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class UserGoalDao(
    private val userGoalRepository: UserGoalRepository,
) {
    fun save(userGoal: UserGoal): UserGoal {
        return userGoalRepository.save(userGoal)
    }

    fun getUserGoal(user: User): UserGoal {
        return userGoalRepository.findByUser(user)
            ?: throw CustomException(ErrorCode.GOAL_NOT_FOUND)
    }

    fun findUserGoal(user: User): UserGoal? {
        return userGoalRepository.findByUser(user)
    }
}
