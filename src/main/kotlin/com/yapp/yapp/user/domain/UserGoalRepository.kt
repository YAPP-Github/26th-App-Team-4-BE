package com.yapp.yapp.user.domain

import org.springframework.data.repository.CrudRepository

interface UserGoalRepository : CrudRepository<UserGoal, Long> {
    fun findByUser(user: User): UserGoal?
}
