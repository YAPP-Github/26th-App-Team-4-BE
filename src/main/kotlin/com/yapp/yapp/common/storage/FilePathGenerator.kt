package com.yapp.yapp.common.storage

import com.yapp.yapp.record.domain.record.RunningRecord

object FilePathGenerator {
    fun generateRunningRecordImagePath(runningRecord: RunningRecord): String {
        return "users/user-${runningRecord.user.id}/record-${runningRecord.id}.jpg"
    }
}
