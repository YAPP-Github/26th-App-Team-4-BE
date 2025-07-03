package com.yapp.yapp.user

import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.response.OnboardingResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders

class OnboardingControllerTest : BaseControllerTest() {
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
}
