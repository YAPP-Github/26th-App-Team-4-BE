package com.yapp.yapp.user.domain

import com.yapp.yapp.record.domain.point.RunningPointRepository
import com.yapp.yapp.record.domain.record.RunningRecordRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class DeletedUserDao(
    private val deletedUserRepository: DeletedUserRepository,
    private val userRepository: UserRepository,
    private val runningRecordRepository: RunningRecordRepository,
    private val runningPointRepository: RunningPointRepository,
) {
    fun save(deletedUser: DeletedUser) {
        deletedUserRepository.save(deletedUser)
    }

    fun delete(userId: Long) {
        deletedUserRepository.deleteById(userId)
    }

    fun cleanup() {
        val cutoffDate = LocalDate.now().minusDays(30)
        val cutoffDateTime = cutoffDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime()

        val deletedUserIds = deletedUserRepository.findIdsByDeletedAtBefore(cutoffDateTime)
        val runningRecords = runningRecordRepository.findAllByUserIdIn(deletedUserIds)

        runningPointRepository.deleteByRunningRecordIn(runningRecords)
        runningRecordRepository.deleteByUserIdIn(deletedUserIds)
        userRepository.deleteByIdIn(deletedUserIds)
    }
}
