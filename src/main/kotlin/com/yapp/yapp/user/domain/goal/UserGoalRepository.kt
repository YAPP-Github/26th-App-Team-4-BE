package com.yapp.yapp.user.domain.goal

import com.yapp.yapp.user.domain.User
import org.springframework.data.repository.CrudRepository

interface UserGoalRepository : CrudRepository<UserGoal, Long> {
    fun findByUser(user: User): UserGoal?
}
