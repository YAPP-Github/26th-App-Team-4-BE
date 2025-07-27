package com.yapp.yapp.support.fixture

import com.yapp.yapp.common.TimeProvider
import com.yapp.yapp.record.domain.Pace
import com.yapp.yapp.record.domain.RunningMetricsCalculator
import com.yapp.yapp.running.api.request.RunningDoneRequest
import com.yapp.yapp.running.api.request.RunningPollingUpdateRequest
import com.yapp.yapp.running.api.request.RunningStartRequest
import com.yapp.yapp.user.api.request.AnalysisFeedbackUpdateRequest
import com.yapp.yapp.user.api.request.AudioCoachingUpdateRequest
import com.yapp.yapp.user.api.request.AudioFeedbackUpdateRequest
import com.yapp.yapp.user.api.request.CrewRankingUpdateRequest
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.api.request.OnboardingRequest
import com.yapp.yapp.user.api.request.PaceGoalRequest
import com.yapp.yapp.user.api.request.PromEventUpdateRequest
import com.yapp.yapp.user.api.request.RemindAlertUpdateRequest
import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

object RequestFixture {
    fun runningStartRequest(
        lat: Double = 37.5665,
        lon: Double = 126.9780,
        timeStamp: String = "2025-06-17T16:12:00+09:00",
    ) = RunningStartRequest(
        lat = lat,
        lon = lon,
        timeStamp = timeStamp,
    )

    fun runningPollingUpdateRequest(
        recordId: Long = 1L,
        lat: Double = 37.5665,
        lon: Double = 126.9780,
        heartRate: Int = 142,
        totalRunningTime: Long = (1 * 60 + 2) * 1000 + 12,
        timeStamp: String = "2025-06-17T16:12:00+09:00",
    ) = RunningPollingUpdateRequest(
        lat = lat,
        lon = lon,
        heartRate = heartRate,
        totalRunningTime = totalRunningTime,
        timeStamp = timeStamp,
    )

    fun runningDoneRequest(
        totalDistance: Double = 1500.0,
        totalCalories: Int = 100,
        startAt: String = "2025-06-17T16:12:03+09:00",
        runningPoints: List<RunningDoneRequest.RunningPointRequest> =
            generateRunningPointsForDistance(
                targetDistanceMeters = totalDistance,
            ),
    ): RunningDoneRequest {
        val totalTime = if (runningPoints.isNotEmpty()) runningPoints.last().totalRunningTimeMills else 0L
        return RunningDoneRequest(
            totalTime = totalTime,
            totalDistance = totalDistance,
            totalCalories = totalCalories,
            averagePace = Pace(distanceMeter = totalDistance, durationMills = totalTime).toMills(),
            startAt = startAt,
            runningPoints = runningPoints,
        )
    }

    fun generateRunningPointsForDistance(
        targetDistanceMeters: Double = 1_000.0,
        startLat: Double = 37.5665,
        startLon: Double = 126.9780,
        startAt: String = "2025-06-17T16:12:00+09:00",
        intervalSeconds: Int = 3,
    ): List<RunningDoneRequest.RunningPointRequest> {
        // 1) 시작 시각 파싱
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val startInstant = OffsetDateTime.parse(startAt, formatter).toInstant()

        // 2) 위도/경도 0.00001° 이동 시 대략 거리 계산
        val perSecondDistance =
            RunningMetricsCalculator.calculateDistance(
                fromLat = startLat,
                fromLon = startLon,
                toLat = startLat + 0.00001,
                toLon = startLon + 0.00001,
            )

        // 3) 거리 기준으로 필요한 초 계산 (올림)
        val secs = ceil(targetDistanceMeters / perSecondDistance).toInt()

        // 5) intervalSeconds 단위로 올림
        val totalSeconds = ((secs + intervalSeconds - 1) / intervalSeconds) * intervalSeconds

        // 6) 0초부터 totalSeconds까지 step 간격으로 포인트 생성
        return (0..totalSeconds step intervalSeconds).map { curSec ->
            RunningDoneRequest.RunningPointRequest(
                lat = startLat + 0.00001 * curSec,
                lon = startLon + 0.00001 * curSec,
                totalRunningTimeMills = TimeProvider.toMills(curSec),
                timeStamp =
                    OffsetDateTime
                        .parse(startAt, formatter)
                        .plusSeconds(curSec.toLong())
                        .toString(),
            )
        }
    }

    fun onboardingRequest(
        answers: List<OnboardingAnswerDto> =
            listOf(
                OnboardingAnswerDto(OnboardingQuestionType.EXPLOSIVE_STRENGTH, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.AGILITY, OnboardingAnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.COORDINATION, OnboardingAnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.BALANCE, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.EXERCISE_EXPERIENCE, OnboardingAnswerLabel.B),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_EXPERIENCE, OnboardingAnswerLabel.C),
                OnboardingAnswerDto(OnboardingQuestionType.RUNNING_ENDURANCE, OnboardingAnswerLabel.A),
                OnboardingAnswerDto(OnboardingQuestionType.PACE_AWARENESS, OnboardingAnswerLabel.B),
            ),
    ): OnboardingRequest {
        return OnboardingRequest(answers)
    }

    fun paceGoalRequest(pace: Long = (7 * 60 + 30) * 1000L): PaceGoalRequest {
        return PaceGoalRequest(pace)
    }

    fun audioCoachingUpdateRequest(audioCoaching: Boolean = true): AudioCoachingUpdateRequest {
        return AudioCoachingUpdateRequest(audioCoaching = audioCoaching)
    }

    fun remindAlertUpdateRequest(remindAlert: Boolean = true): RemindAlertUpdateRequest {
        return RemindAlertUpdateRequest(remindAlert = remindAlert)
    }

    fun audioFeedbackUpdateRequest(audioFeedback: Boolean = true): AudioFeedbackUpdateRequest {
        return AudioFeedbackUpdateRequest(audioFeedback = audioFeedback)
    }

    fun analysisFeedbackUpdateRequest(analysisFeedback: Boolean = true): AnalysisFeedbackUpdateRequest {
        return AnalysisFeedbackUpdateRequest(analysisFeedback = analysisFeedback)
    }

    fun crewRankingUpdateRequest(crewRanking: Boolean = true): CrewRankingUpdateRequest {
        return CrewRankingUpdateRequest(crewRanking = crewRanking)
    }

    fun promEventUpdateRequest(promEvent: Boolean = true): PromEventUpdateRequest {
        return PromEventUpdateRequest(promEvent = promEvent)
    }
}
