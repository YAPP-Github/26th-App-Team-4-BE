package com.yapp.yapp.user

import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.user.api.response.UserResponse
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

class UserControllerTest : BaseControllerTest() {
    @Test
    fun `유저를 조회한다`() {
        // given
        val email = "test@test.com"
        val accessToken = getAccessToken(email)

        // when
        // then
        val result =
            RestAssured.given().log().all()
                .header("Authorization", "$accessToken")
                .`when`().get("/api/v1/users")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val response = objectMapper.convertValue(result, UserResponse::class.java)

        assertAll(
            { Assertions.assertThat(response.id).isNotNull() },
            { Assertions.assertThat(response.email).isEqualTo(email) },
            { Assertions.assertThat(response.nickname).isNotNull() },
        )
    }

    @Test
    fun `회원탈퇴를 한다`() {
        // given
        val accessToken = getAccessToken("test@test.com")
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", "$accessToken")
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)
    }

    @Test
    fun `회원탈퇴 후 조회`() {
        // given
        val accessToken = getAccessToken("test@test.com")
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", "$accessToken")
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)

        RestAssured.given().log().all()
            .header("Authorization", "$accessToken")
            .`when`().get("/api/v1/users")
            .then().log().all()
            .statusCode(400)
    }
}
