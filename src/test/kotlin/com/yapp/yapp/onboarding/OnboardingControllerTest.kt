package com.yapp.yapp.onboarding

import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import io.restassured.RestAssured
import io.restassured.http.ContentType
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
            .statusCode(200)

        // then
        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .body(request)
            .`when`().get("/api/v1/users/onboarding")
            .then().log().all()
            .statusCode(200)
    }
}
