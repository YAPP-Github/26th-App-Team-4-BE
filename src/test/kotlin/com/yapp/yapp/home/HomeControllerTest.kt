package com.yapp.yapp.home

import com.yapp.yapp.support.BaseControllerTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class HomeControllerTest : BaseControllerTest() {
    @Test
    fun `러닝 목표가 없는 유저의 홈 화면에서 목표는 null이다 `() {
        // given
        val email = "test@test.com"
        val user = userFixture.createWithPurposeGoal(email)

        // when
        // then
        RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(user.email))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .`when`().get("/api/v1/home")
            .then().log().all()
            .statusCode(200)
    }
}
