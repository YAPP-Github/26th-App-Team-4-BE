package com.yapp.yapp.record

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.RecordsSearchType
import com.yapp.yapp.record.domain.RunningRecordService
import com.yapp.yapp.support.BaseServiceTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable

class RunningRecordServiceTest : BaseServiceTest() {
    @Autowired
    lateinit var runningRecordService: RunningRecordService

    @Test
    fun `삭제된 기록은 조회되지 않는다`() {
        // given
        val user = userFixture.createWithGoal()
        val runningRecord = runningFixture.createRunningRecord(user = user)

        // when
        runningRecordService.deleteRecord(userId = user.id, recordId = runningRecord.id)

        // then
        val result =
            runningRecordService.getRecords(
                userId = user.id,
                type = RecordsSearchType.ALL.name,
                targetDate = TimeProvider.now(),
                pageable = Pageable.unpaged(),
            )
        Assertions.assertThat(result.records).hasSize(0)
    }
}
