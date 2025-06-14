package com.yapp.yapp.running

import com.yapp.yapp.running.request.RunningStartRequest
import com.yapp.yapp.running.response.RunningStartResponse
import org.springframework.stereotype.Service

@Service
class RunningService(
    private val runningRecordRepository: RunningRecordRepository,
    private val runningPointRepository: RunningPointRepository,
) {
    fun start(request: RunningStartRequest): RunningStartResponse {
        // TODO 유저 정보 조회
        val runningRecord = runningRecordRepository.save(RunningRecord())
        val runningPoint =
            runningPointRepository.save(
                RunningPoint(
                    runningRecord = runningRecord,
                    lat = request.lat,
                    lon = request.lon,
                ),
            )
        return RunningStartResponse(runningRecord.id)
    }
}
