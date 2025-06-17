package com.yapp.yapp.running

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

/**
 * TODO
 * Repository와 Service 사이에 Dao를 두려고 합니다.
 * 그 근거는 다음과 같습니다.
 * 1. Repository 인터페이스에 JPA만 정의하도록 분리하여 책임을 분리합니다.
 * 2. JPA 메서드 결과가 null일 수 있기에 Null Safe한 로직을 분리할 필요성을 느꼈습니다.
 * 매번 find한 결과가 null인 경우 예외처리하는 로직을 Service에 두게 된다면 추후 유지보수에 리소스가 클 것 같았습니다.
 * 그래서 DAO 계층에 해당 로직을 구현하려고 합니다.
 */
@Component
class RunningRecordDao(
    private val runningRecordRepository: RunningRecordRepository,
) {
    fun save(runningRecord: RunningRecord): RunningRecord {
        return runningRecordRepository.save(runningRecord)
    }

    fun getById(id: Long): RunningRecord {
        return runningRecordRepository.findById(id).orElseThrow { throw CustomException(ErrorCode.RECORD_NOT_FOUND) }
    }
}
