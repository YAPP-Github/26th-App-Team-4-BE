package com.yapp.yapp.user

import com.deepromeet.atcha.support.BaseControllerTest
import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.fixture.IdTokenFixture
import com.yapp.yapp.user.api.response.UserResponse
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean

class UserControllerTest : BaseControllerTest() {
    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

    @Test
    fun `유저를 조회한다`() {
        // given
        val loginResponse = loginUser()

        // when
        // then
        val result =
            RestAssured.given().log().all()
                .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
                .`when`().get("/api/v1/users")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val response = objectMapper.convertValue(result, UserResponse::class.java)

        assertAll(
            { Assertions.assertThat(response.id).isEqualTo(loginResponse.user.id) },
            { Assertions.assertThat(response.email).isEqualTo(loginResponse.user.email) },
            { Assertions.assertThat(response.name).isEqualTo(loginResponse.user.name) },
            { Assertions.assertThat(response.profile).isEqualTo(loginResponse.user.profile) },
        )
    }

    @Test
    fun `회원탈퇴를 한다`() {
        // given
        val loginResponse = loginUser()
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)
    }

    @Test
    fun `회원탈퇴 후 조회`() {
        // given
        val loginResponse = loginUser()
        // when
        // then
        RestAssured.given().log().all()
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().delete("/api/v1/users")
            .then().log().all()
            .statusCode(204)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer ${loginResponse.tokenResponse.accessToken}")
            .`when`().get("/api/v1/users")
            .then().log().all()
            .statusCode(400)
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
