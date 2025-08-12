package com.yapp.yapp.document.user

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.request.DistanceGoalRequest
import com.yapp.yapp.user.api.request.RunningPurposeRequest
import com.yapp.yapp.user.api.request.TimeGoalRequest
import com.yapp.yapp.user.api.request.WeeklyRunCountGoalRequest
import com.yapp.yapp.user.domain.goal.RunningPurposeAnswerLabel
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import java.time.Duration

class UserGoalDocumentTest : BaseDocumentTest() {
    @Test
    fun `주간 달리기 횟수 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("count").description("목표 달리기 횟수"),
                    fieldWithPath("remindAlert").description("리마인더 알림 설정(on인 경우 true"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.weeklyRunningCount").description("주간 달리기 횟수"),
                )

        val restDocsFilter =
            filter("goal", "weekly-running-count-save")
                .tag(Tag.GOAL_API)
                .summary("주간 달리기 횟수 목표 설정 API")
                .description("주간 달리기 횟수 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = WeeklyRunCountGoalRequest(count = 3, remindAlert = true)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/weekly-run-count")
            .then()
            .statusCode(201)
    }

    @Test
    fun `페이스 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("pace").description("목표 페이스 밀리초 단위"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.paceGoal").description("페이스 목표 시간 밀리초 단위"),
                )

        val restDocsFilter =
            filter("goal", "pace-save")
                .tag(Tag.GOAL_API)
                .summary("페이스 목표 설정 API")
                .description("페이스 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = RequestFixture.paceGoalRequest()
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/pace")
            .then()
            .statusCode(201)
    }

    @Test
    fun `거리 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("distanceMeter").description("목표 거리(m)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                )

        val restDocsFilter =
            filter("goal", "distance-save")
                .tag(Tag.GOAL_API)
                .summary("거리 목표 설정 API")
                .description("거리 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = DistanceGoalRequest(distanceMeter = 5000.0)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/distance")
            .then()
            .statusCode(201)
    }

    @Test
    fun `시간 목표 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("time").description("목표 시간 밀리초 단위"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.timeGoal").description("시간 목표 시간 밀리초 단위"),
                )

        val restDocsFilter =
            filter("goal", "time-save")
                .tag(Tag.GOAL_API)
                .summary("시간 목표 설정 API")
                .description("시간 목표를 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = TimeGoalRequest(time = Duration.ofSeconds(40 * 60 + 30).toMillis())
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/time")
            .then()
            .statusCode(201)
    }

    @Test
    fun `러닝 목적 설정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("runningPurpose").description(
                        "러닝 목적을 문자열로 받습니다. " +
                            "(다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION",
                    ),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningPurpose").description(
                        "달리기 목적 (" +
                            "다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION",
                    ),
                )

        val restDocsFilter =
            filter("goal", "running-purpose-save")
                .tag(Tag.GOAL_API)
                .summary("러닝 목적 설정 API")
                .description("러닝 목적을 설정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = RunningPurposeRequest(runningPurpose = RunningPurposeAnswerLabel.WEIGHT_LOSS_PURPOSE)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().post("/api/v1/users/goals/purpose")
            .then()
            .statusCode(201)
    }

    @Test
    fun `목표 조회 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningPurpose").description(
                        "달리기 목적 (" +
                            "다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION",
                    ),
                    fieldWithPath("result.weeklyRunningCount").description("주간 달리기 횟수"),
                    fieldWithPath("result.paceGoal").description("페이스 목표 시간 밀리초 단위"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                    fieldWithPath("result.timeGoal").description("시간 목표 시간 밀리초 단위"),
                    fieldWithPath("result.runnerType").description("러너 타입(초보: BEGINNER, 중급: INTERMEDIATE, 전문가: EXPERT)"),
                )

        val restDocsFilter =
            filter("goal", "goal-search")
                .tag(Tag.GOAL_API)
                .summary("목표 조회 API")
                .description("목표를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        userGoalFixture.create(user)

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .`when`().get("/api/v1/users/goals")
            .then()
            .statusCode(200)
    }

    @Test
    fun `추천 페이스 조회 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.recommendPace").description("추천 페이스(밀리초)"),
                )

        val restDocsFilter =
            filter("goal", "recommend-pace-search")
                .tag(Tag.GOAL_API)
                .summary("추천 페이스 조회 API")
                .description("추천 페이스를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        userGoalFixture.create(user)

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .`when`().get("/api/v1/users/goals/pace/recommend")
            .then()
            .statusCode(200)
    }

    @Test
    fun `주간 달리기 횟수 목표 수정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("count").description("목표 달리기 수"),
                    fieldWithPath("remindAlert").description("리마인더 알림 설정(on인 경우 true"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.weeklyRunningCount").description("주간 달리기 횟수"),
                )

        val restDocsFilter =
            filter("goal", "weekly-running-count-update")
                .tag(Tag.GOAL_API)
                .summary("주간 달리기 횟수 목표 수정 API")
                .description("주간 달리기 횟수 목표를 수정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = WeeklyRunCountGoalRequest(count = 3, remindAlert = true)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().patch("/api/v1/users/goals/weekly-run-count")
            .then()
            .statusCode(200)
    }

    @Test
    fun `페이스 목표 수정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("pace").description("목표 페이스 밀리초 단위"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.paceGoal").description("페이스 목표 시간 밀리초 단위"),
                )

        val restDocsFilter =
            filter("goal", "pace-update")
                .tag(Tag.GOAL_API)
                .summary("페이스 목표 수정 API")
                .description("페이스 목표를 수정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = RequestFixture.paceGoalRequest()
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().patch("/api/v1/users/goals/pace")
            .then()
            .statusCode(200)
    }

    @Test
    fun `거리 목표 수정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("distanceMeter").description("목표 거리(m)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.distanceMeterGoal").description("거리 목표(m)"),
                )

        val restDocsFilter =
            filter("goal", "distance-update")
                .tag(Tag.GOAL_API)
                .summary("거리 목표 수정 API")
                .description("거리 목표를 수정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = DistanceGoalRequest(distanceMeter = 5000.0)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().patch("/api/v1/users/goals/distance")
            .then()
            .statusCode(200)
    }

    @Test
    fun `시간 목표 수정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("time").description("목표 시간 밀리초 단위"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.timeGoal").description("시간 목표 시간 밀리초 단위"),
                )

        val restDocsFilter =
            filter("goal", "time-update")
                .tag(Tag.GOAL_API)
                .summary("시간 목표 수정 API")
                .description("시간 목표를 수정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = TimeGoalRequest(time = Duration.ofSeconds(40 * 60 + 30).toMillis())
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().patch("/api/v1/users/goals/time")
            .then()
            .statusCode(200)
    }

    @Test
    fun `러닝 목적 수정 API`() {
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("runningPurpose").description(
                        "러닝 목적을 문자열로 받습니다. " +
                            "(다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION",
                    ),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.goalId").description("목표 ID"),
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runningPurpose").description(
                        "달리기 목적 (" +
                            "다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION",
                    ),
                )

        val restDocsFilter =
            filter("goal", "running-purpose-update")
                .tag(Tag.GOAL_API)
                .summary("러닝 목적 수정 API")
                .description("러닝 목적을 수정합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()

        // when
        // then
        val request = RunningPurposeRequest(runningPurpose = RunningPurposeAnswerLabel.WEIGHT_LOSS_PURPOSE)
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", getAccessToken(email = user.email))
            .body(request)
            .`when`().patch("/api/v1/users/goals/purpose")
            .then()
            .statusCode(200)
    }
}
