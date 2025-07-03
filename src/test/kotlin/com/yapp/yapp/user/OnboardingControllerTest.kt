package com.yapp.yapp.user

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.request.OnboardingAnswerDto
import com.yapp.yapp.user.api.response.OnboardingResponse
import com.yapp.yapp.user.domain.onboarding.AnswerLabel
import com.yapp.yapp.user.domain.onboarding.OnboardingDao
import com.yapp.yapp.user.domain.onboarding.OnboardingQuestionType
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders

class OnboardingControllerTest : BaseControllerTest() {
    @Autowired
    lateinit var onboardingDao: OnboardingDao

    @Test
    fun `온보딩을 조회한다`() {
        // given
        val user = userFixture.create()
        val request = RequestFixture.onboardingRequest()

        // when
        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .body(request)
            .`when`().post("/api/v1/users/onboarding")
            .then().log().all()
            .statusCode(201)

        // then
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
                .body(request)
                .`when`().get("/api/v1/users/onboarding")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val response = convert(result, OnboardingResponse::class.java)

        Assertions.assertThat(response.answerList).hasSize(request.answers.size)
    }

    @Test
    fun `온보딩 대답을 수정한다`() {
        // given
        val user = userFixture.create()
        val questionType = OnboardingQuestionType.EXPLOSIVE_STRENGTH
        val saveRequest =
            RequestFixture.onboardingRequest(
                answers =
                    listOf(
                        OnboardingAnswerDto(questionType, AnswerLabel.A),
                    ),
            )
        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .body(saveRequest)
            .`when`().post("/api/v1/users/onboarding")
            .then().log().all()
            .statusCode(201)

        // when
        val switchedAnswer = AnswerLabel.C
        val request =
            RequestFixture.onboardingRequest(
                answers =
                    listOf(
                        OnboardingAnswerDto(questionType, switchedAnswer),
                    ),
            )
        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .body(request)
            .`when`().patch("/api/v1/users/onboarding")
            .then().log().all()
            .statusCode(200)

        // then
        val actual = onboardingDao.getAnswerByUser(user, questionType).answer
        Assertions.assertThat(actual).isEqualTo(switchedAnswer)
    }
}
