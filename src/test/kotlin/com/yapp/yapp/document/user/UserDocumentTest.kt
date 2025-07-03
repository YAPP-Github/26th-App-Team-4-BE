package com.yapp.yapp.document.user

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.document.Tag
import com.yapp.yapp.document.support.BaseDocumentTest
import com.yapp.yapp.support.fixture.IdTokenFixture
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.domain.onboarding.OnboardAnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.context.bean.override.mockito.MockitoBean

class UserDocumentTest : BaseDocumentTest() {
    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

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
                    fieldWithPath("result.id").description("사용자 ID"),
                    fieldWithPath("result.email").description("사용자 이메일"),
                    fieldWithPath("result.name").description("사용자 이름"),
                    fieldWithPath("result.profileImage").description("사용자 프로필"),
                    fieldWithPath("result.provider").description("소셜 로그인 유형"),
                )

        val restDocsFilter =
            filter("사용자 API", "조회")
                .tag(Tag.USER_API)
                .summary("사용자 조회 API")
                .description("액세스 토큰을 통해 사용자 정보를 조회합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()

        val loginResponse = loginUser()

        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
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
            filter("사용자 API", "회원 탈퇴")
                .tag(Tag.USER_API)
                .summary("사용자 회원 탈퇴 API")
                .description("액세스 토큰을 통해 회원 탈퇴 합니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()
        // when
        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
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
                    fieldWithPath("answers[].answer").description("답변 타입(Enum). 답변에 따라 A, B, C, D 중 하나입니다."),
                )

        val restDocsResponse =
            response()

        val restDocsFilter =
            filter("사용자 API", "온보딩 저장")
                .tag(Tag.USER_API)
                .summary("온보딩 저장 API")
                .description("온보딩 설문 조사를 저장하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()

        // when
        val request = RequestFixture.onboardingRequest()

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
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
                    ).description("응답 타입(Enum) 답변에 따라 A, B, C, D 중 하나입니다. 러닝 목표만 선택지가 4개이고 나머진 3개입니다."),
                )

        val restDocsFilter =
            filter("사용자 API", "온보딩 조회")
                .tag(Tag.USER_API)
                .summary("온보딩 조회 API")
                .description("온보딩 설문 조사를 조회하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()
        val request = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)
        // when

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
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
                    fieldWithPath("answers[].answer").description("답변 타입(Enum). 답변에 따라 A, B, C, D 중 하나입니다."),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.userId").description("유저 ID"),
                    fieldWithPath("result.answerList").description("온보딩 설문조사 리스트"),
                    fieldWithPath("result.answerList[].questionType").description("질문 타입(Enum)"),
                    fieldWithPath(
                        "result.answerList[].answer",
                    ).description("답변 타입(Enum). 답변에 따라 A, B, C, D 중 하나입니다. 러닝 목표만 선택지가 4개이고 나머진 3개입니다."),
                )

        val restDocsFilter =
            filter("사용자 API", "온보딩 수정")
                .tag(Tag.USER_API)
                .summary("온보딩 수정 API")
                .description("온보딩 설문 조사를 수정하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()
        val saveRequest = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(saveRequest)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)

        // when
        val request =
            RequestFixture.onboardingRequest(
                answers =
                    listOf(
                        OnboardingAnswerDto(OnboardingQuestionType.GOAL, OnboardAnswerLabel.D),
                    ),
            )

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().patch("/api/v1/users/onboarding")
            .then()
            .statusCode(200)
    }

    @Test
    fun `러닝 목표 조회 API`() {
        // given
        val restDocsRequest =
            request()
                .requestHeader(
                    headerWithName("Authorization").description("엑세스 토큰 (Bearer)"),
                )

        val restDocsResponse =
            response()
                .responseBodyFieldWithResult(
                    fieldWithPath("result.questionType").description("질문 타입(Enum)"),
                    fieldWithPath("result.answer").description("응답 타입(Enum) A(다이어트), B(건강 관리), C(체력 증진), D(대회 준비) 중 하나입니다."),
                )

        val restDocsFilter =
            filter("사용자 API", "목표 조회")
                .tag(Tag.USER_API)
                .summary("목표 조회 API")
                .description("러닝 목표를 조회하는 API 입니다.")
                .request(restDocsRequest)
                .response(restDocsResponse)
                .build()
        val loginResponse = loginUser()
        val request = RequestFixture.onboardingRequest()
        RestAssured.given()
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().post("/api/v1/users/onboarding")
            .then()
            .statusCode(201)
        // when

        // then
        RestAssured.given(spec)
            .filter(restDocsFilter)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().get("/api/v1/users/onboarding/goal")
            .then()
            .statusCode(200)
    }

    private fun loginUser(): LoginResponse {
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null, null)

        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val loginResponse = objectMapper.convertValue(result, LoginResponse::class.java)
        return loginResponse
    }
}
