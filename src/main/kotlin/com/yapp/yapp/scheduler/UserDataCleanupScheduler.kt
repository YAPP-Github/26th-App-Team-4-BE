package com.yapp.yapp.scheduler

import com.yapp.yapp.user.domain.DeletedUserDao
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserDataCleanupScheduler(
    private val deletedUserDao: DeletedUserDao,
) {
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    fun cleanupDeletedUsers() {
        deletedUserDao.cleanup()
    }
}
