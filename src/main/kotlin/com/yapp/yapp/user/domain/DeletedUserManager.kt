package com.yapp.yapp.user.domain

import com.yapp.yapp.record.domain.point.RunningPointDao
import com.yapp.yapp.record.domain.record.RunningRecordDao
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class DeletedUserManager(
    private val userDao: UserDao,
    private val runningRecordDao: RunningRecordDao,
    private val runningPointDao: RunningPointDao,
    private val deletedUserDao: DeletedUserDao,
) {
    fun cleanup() {
        val cutoffDate = LocalDate.now().minusDays(30)
        val cutoffDateTime = cutoffDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime()

        val userIds = deletedUserDao.getDeletedUserIds(cutoffDateTime)
        if (userIds.isEmpty()) {
            return
        }
        val runningRecordIds = runningRecordDao.getIdsByUserIdIn(userIds)

        runningPointDao.deleteByRunningRecordIdIn(runningRecordIds)
        runningRecordDao.deleteByUserIdIn(userIds)
        deletedUserDao.deleteByUserIdIn(userIds)
        userDao.deleteByIdIn(userIds)
    }
}
