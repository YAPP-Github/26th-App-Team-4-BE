package com.yapp.yapp.home

import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.user.api.request.RunningPurposeRequest
import com.yapp.yapp.user.domain.goal.RunningPurposeAnswerLabel
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class HomeControllerTest : BaseControllerTest() {
    @Test
    fun `러닝 목표가 없는 유저의 목표는 null이다 `() {
        // given
        val email = "test@test.com"
        val user = userFixture.create(email)
        val accessToken = getAccessToken(user.email)

        val request = RunningPurposeRequest(RunningPurposeAnswerLabel.COMPETITION_PREPARATION)
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/v1/users/goals/purpose")
            .then().log().all()
            .statusCode(201)

        // when
        // then
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .`when`().get("/api/v1/home")
            .then().log().all()
            .statusCode(200)
    }
}
