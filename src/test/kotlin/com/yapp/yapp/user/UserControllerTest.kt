package com.yapp.yapp.user

import com.deepromeet.atcha.support.BaseControllerTest
import com.deepromeet.atcha.support.fixture.UserFixture
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.common.exception.ErrorCode
import com.yapp.yapp.user.api.request.UserCreateRequest
import com.yapp.yapp.user.domain.User
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders

class UserControllerTest : BaseControllerTest() {
    @Test
    fun `유저를 생성한다`() {
        // given
        val user = UserFixture.create()
        val request = UserCreateRequest(user.name, user.email, user.profile)

        // when
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(request)
                .`when`().post("/api/users")
                .then().log().all()
                .statusCode(201)
                .extract().`as`(ApiResponse::class.java)
                .result
        val resultUser = objectMapper.convertValue(result, User::class.java)

        // then
        assertAll(
            { Assertions.assertThat(resultUser.name).isEqualTo(user.name) },
            { Assertions.assertThat(resultUser.email).isEqualTo(user.email) },
            { Assertions.assertThat(resultUser.profile).isEqualTo(user.profile) },
        )
    }

    @Test
    fun `유저를 조회한다`() {
        // given
        val saved = userRepository.save(UserFixture.create())

        // when
        // then
        RestAssured.given().log().all()
            .`when`().get("/api/users/${saved.id}")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `존재하지 않은 유저 조회시 에러가 발생한다`() {
        // given
        // when
        val responseCode =
            RestAssured.given().log().all()
                .pathParam("id", 1)
                .`when`().get("/api/users/{id}")
                .then().log().all()
                .statusCode(400)
                .extract()
                .`as`(ApiResponse::class.java).code

        // then
        Assertions.assertThat(responseCode).isEqualTo(ErrorCode.USER_NOT_FOUND.errorCode)
    }
}
