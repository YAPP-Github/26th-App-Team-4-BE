package com.yapp.yapp.user

import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.RequestFixture
import com.yapp.yapp.user.api.response.UserAndGoalResponse
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders

class UserControllerTest : BaseControllerTest() {
    @Test
    fun `목표가 있는 유저를 조회한다`() {
        // given
        val email = "test@test.com"
        val user = userFixture.create(email)
        val accessToken = getAccessToken(email)
        val userGoal = userGoalFixture.create(user)

        // when
        // then
        val result =
            RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .`when`().get("/api/v1/users")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val response = objectMapper.convertValue(result, UserAndGoalResponse::class.java)

        assertAll(
            { Assertions.assertThat(response.user.userId).isNotNull() },
            { Assertions.assertThat(response.user.email).isEqualTo(email) },
            { Assertions.assertThat(response.user.nickname).isNotNull() },
            { Assertions.assertThat(response.user.provider).isNotNull() },
            { Assertions.assertThat(response.user.runnerType).isNotNull() },
            { Assertions.assertThat(response.goal!!.userId).isNotNull() },
            { Assertions.assertThat(response.goal!!.goalId).isEqualTo(userGoal.id) },
            { Assertions.assertThat(response.goal!!.paceGoal).isNotNull() },
            { Assertions.assertThat(response.goal!!.timeGoal).isNotNull() },
            { Assertions.assertThat(response.goal!!.runningPurpose).isNotNull() },
            { Assertions.assertThat(response.goal!!.distanceMeterGoal).isNotNull() },
            { Assertions.assertThat(response.goal!!.weeklyRunningCount).isNotNull() },
        )
    }

    @Test
    fun `목표가 없는 유저를 조회한다`() {
        // given
        val email = "test@test.com"
        val user = userFixture.create(email = email)
        val accessToken = getAccessToken(user.email)

        // when
        // then
        val result =
            RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .`when`().get("/api/v1/users")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val response = objectMapper.convertValue(result, UserAndGoalResponse::class.java)

        assertAll(
            { Assertions.assertThat(response.user.userId).isNotNull() },
            { Assertions.assertThat(response.user.email).isEqualTo(email) },
            { Assertions.assertThat(response.user.nickname).isNotNull() },
            { Assertions.assertThat(response.user.provider).isNotNull() },
            { Assertions.assertThat(response.user.runnerType).isNotNull() },
            { Assertions.assertThat(response.goal).isNull() },
        )
    }

    @Test
    fun `회원 탈퇴를 한다`() {
        // given
        val accessToken = getAccessToken("test@test.com")
        val withdrawRequest = RequestFixture.withDrawRequest("회원 탈퇴 하고 싶음")
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(withdrawRequest)
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)
    }

    @Test
    fun `회원 탈퇴 후 조회`() {
        // given
        val accessToken = getAccessToken("test@test.com")
        val withdrawRequest = RequestFixture.withDrawRequest("회원 탈퇴 하고 싶음")
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(withdrawRequest)
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)

        RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .`when`().get("/api/v1/users")
            .then().log().all()
            .statusCode(400)
    }

    @Test
    fun `회원 탈퇴 후 재로그인`() {
        // given
        val email = "test@test.com"
        val accessToken = getAccessToken("test@test.com")
        val withdrawRequest = RequestFixture.withDrawRequest("회원 탈퇴 하고 싶음")
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", accessToken)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(withdrawRequest)
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)

        val response = getLoginResponse(email = email)
        assertAll(
            { Assertions.assertThat(response.user.userId).isNotNull() },
            { Assertions.assertThat(response.user.email).isEqualTo(email) },
            { Assertions.assertThat(response.user.email).isNotNull() },
            { Assertions.assertThat(response.user.provider).isNotNull() },
            { Assertions.assertThat(response.isNew).isFalse() },
        )
    }
}
