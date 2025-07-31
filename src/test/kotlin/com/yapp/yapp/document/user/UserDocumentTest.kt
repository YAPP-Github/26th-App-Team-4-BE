package com.yapp.yapp.document.user

import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.domain.onboarding.OnboardingAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

class UserDocumentTest : BaseDocumentTest() {
    @Test
    fun `사용자 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.user.userId").description("사용자 ID"),
                    fieldWithPath("result.user.email").description("사용자 이메일"),
                    fieldWithPath("result.user.nickname").description("사용자 닉네임"),
                    fieldWithPath("result.user.provider").description("소셜 로그인 유형 (카카오: KAKAO, 애플: APPLE )"),
                    fieldWithPath("result.user.runnerType").description(
                        "사용자 러너 유형 (" +
                            "초보: BEGINNER, " +
                            "중급: INTERMEDIATE, " +
                            "전문가: EXPORT )",
                    ).type(JsonFieldType.STRING)
                        .optional(),
                    fieldWithPath("result.goal").description("사용자의 목표 정보 (없을 경우 null)").optional(),
                    fieldWithPath("result.goal.goalId").description("목표 ID").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.userId").description("사용자 ID").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.runningPurpose").description(
                        "달리기 목적 (" +
                            "다이어트: WEIGHT_LOSS_PURPOSE, " +
                            "건강 유지: HEALTH_MAINTENANCE_PURPOSE, " +
                            "체력 증진: DAILY_STRENGTH_IMPROVEMENT, " +
                            "대회 준비: COMPETITION_PREPARATION )",
                    ).type(JsonFieldType.STRING).optional(),
                    fieldWithPath("result.goal.weeklyRunningCount").description("주간 달리기 횟수").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.paceGoal").description("페이스 목표 시간 밀리초 단위").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.distanceMeterGoal").description("거리 목표(m)").type(JsonFieldType.NUMBER).optional(),
                    fieldWithPath("result.goal.timeGoal").description("시간 목표 시간 밀리초 단위").type(JsonFieldType.NUMBER).optional(),
                )

        val restDocsFilter =
            filter("user", "search")
                .tag(Tag.USER_API)
                .summary("사용자 조회 API")
                .description("액세스 토큰을 통해 사용자 정보를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val user = userFixture.create()
        val accessToken = getAccessToken(user.email)
        userGoalFixture.create(user)

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", accessToken)
            .`when`().get("/api/v1/users")
            .then()
            .statusCode(200)
    }

    @Test
    fun `회원 탈퇴 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()

        val restDocsFilter =
            filter("user", "withdraw")
                .tag(Tag.USER_API)
                .summary("사용자 회원 탈퇴 API")
                .description("액세스 토큰을 통해 회원 탈퇴 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val accessToken = getAccessToken()
        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", accessToken)
            .`when`().delete("/api/v1/users")
            .then()
            .statusCode(204)
    }

    @Test
    fun `온보딩 저장 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("answers").description("온보딩 설문조사 리스트"),
                    fieldWithPath("answers[].questionType").description("질문 타입(Enum)"),
                    fieldWithPath("answers[].answer").description("답변 타입(Enum). 답변에 따라 A, B, C 중 하나입니다."),
                )

        val restDocsResponse =
            response()

        val restDocsFilter =
            filter("user", "onboarding-save")
                .tag(Tag.USER_API)
                .summary("온보딩 저장 API")
                .description("온보딩 설문 조사를 저장하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val accessToken = getAccessToken()

        // when
        val request = RequestFixture.onboardingRequest()

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)
    }

    @Test
    fun `온보딩 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.answerList").description("온보딩 설문조사 리스트"),
                    fieldWithPath("result.answerList[].questionType").description("질문 타입(Enum)"),
                    fieldWithPath(
                        "result.answerList[].answer",
                    ).description("응답 타입(Enum) 답변에 따라 A, B, C 중 하나입니다."),
                )

        val restDocsFilter =
            filter("user", "onboarding-search")
                .tag(Tag.USER_API)
                .summary("온보딩 조회 API")
                .description("온보딩 설문 조사를 조회하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val accessToken = getAccessToken()
        val request = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)
        // when

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().get("/api/v1/users/onboarding")
            .then()
            .statusCode(200)
    }

    @Test
    fun `온보딩 수정 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )
                .requestBodyField(
                    fieldWithPath("answers").description("수정할 온보딩 설문조사 리스트"),
                    fieldWithPath("answers[].questionType").description("질문 타입(Enum)"),
                    fieldWithPath("answers[].answer").description("답변 타입(Enum). 답변에 따라 A, B, C 중 하나입니다."),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.answerList").description("온보딩 설문조사 리스트"),
                    fieldWithPath("result.answerList[].questionType").description("질문 타입(Enum)"),
                    fieldWithPath(
                        "result.answerList[].answer",
                    ).description("답변 타입(Enum). 답변에 따라 A, B, C 중 하나입니다."),
                )

        val restDocsFilter =
            filter("user", "onboarding-update")
                .tag(Tag.USER_API)
                .summary("온보딩 수정 API")
                .description("온보딩 설문 조사를 수정하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val accessToken = getAccessToken()
        val saveRequest = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(saveRequest)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)

        // when
        val request =
            RequestFixture.onboardingRequest(
                answers =
                    listOf(
                        OnboardingAnswerDto(OnboardingQuestionType.RUNNING_EXPERIENCE, OnboardingAnswerLabel.C),
                    ),
            )

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().patch("/api/v1/users/onboarding")
            .then()
            .statusCode(200)
    }

    @Test
    fun `러너 타입 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("사용자 ID"),
                    fieldWithPath("result.runnerType").description("러너 유형(Enum). 초보: 워밍업, 중급: 루틴, 고급: 챌린저"),
                )

        val restDocsFilter =
            filter("user", "runner-type-search")
                .tag(Tag.USER_API)
                .summary("러너 타입 조회 API")
                .description("사용자의 러너 타입을 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val accessToken = getAccessToken()
        val saveRequest = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(saveRequest)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", accessToken)
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", accessToken)
            .`when`().get("/api/v1/users/type")
            .then()
            .statusCode(200)
    }
}
