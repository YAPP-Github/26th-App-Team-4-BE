package com.yapp.yapp.scheduler

import com.yapp.yapp.user.domain.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class UserDataCleanupScheduler(
    private val userService: UserService,
) {
    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupDeletedUsers() {
        userService.cleanup()
    }
}
