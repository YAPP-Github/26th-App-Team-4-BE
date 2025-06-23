package com.yapp.yapp.auth.api.controller

import com.deepromeet.atcha.support.BaseControllerTest
import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.fixture.IdTokenFixture
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

class AuthControllerTest : BaseControllerTest() {
    @MockitoBean
    lateinit var kakaoFeignClient: KakaoFeignClient

    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

    @DisplayName("로그인 테스트")
    @Nested
    inner class LoginTests {
        @Test
        fun `카카오 로그인`() {
            // given
            val idToken = IdTokenFixture.createValidIdToken(issuer = "https://kauth.kakao.com")
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null, "kakao-initial-test-user")

            // when
            Mockito.`when`(kakaoFeignClient.fetchJwks())
                .thenReturn(objectMapper.writeValueAsString(jwksResponse))

            val result =
                RestAssured.given().log().all()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(loginRequest)
                    .`when`().post("/api/v1/auth/login/kakao")
                    .then().log().all()
                    .statusCode(200)
                    .extract().`as`(ApiResponse::class.java)
                    .result

            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isEqualTo(loginRequest.name) },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `애플 로그인`() {
            // given
            val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null, "apple-initial-test-user")

            // when
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

            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isEqualTo(loginRequest.name) },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `카카오 로그인 with nonce`() {
            // given
            val nonce = UUID.randomUUID().toString()
            val idToken =
                IdTokenFixture.createValidIdToken(issuer = "https://kauth.kakao.com", nonce = nonce)
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, nonce, "kakao-initial-test-user-with-nonce")

            // when
            Mockito.`when`(kakaoFeignClient.fetchJwks())
                .thenReturn(objectMapper.writeValueAsString(jwksResponse))

            val result =
                RestAssured.given().log().all()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(loginRequest)
                    .`when`().post("/api/v1/auth/login/kakao")
                    .then().log().all()
                    .statusCode(200)
                    .extract().`as`(ApiResponse::class.java)
                    .result

            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isEqualTo(loginRequest.name) },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `애플 로그인 with nonce`() {
            // given
            val nonce = UUID.randomUUID().toString()
            val idToken =
                IdTokenFixture.createValidIdToken(
                    issuer = "https://appleid.apple.com",
                    nonce = nonce,
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, nonce, "apple-initial-test-user-with-nonce")

            // when
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

            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isEqualTo(loginRequest.name) },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `회원가입 후 로그인`() {
            // given
            val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null, null)

            // when
            Mockito.`when`(appleFeignClient.fetchJwks())
                .thenReturn(objectMapper.writeValueAsString(jwksResponse))

            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

            val result =
                RestAssured.given().log().all()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(loginRequest)
                    .`when`().post("/api/v1/auth/login/apple")
                    .then().log().all()
                    .statusCode(200)
                    .extract().`as`(ApiResponse::class.java)
                    .result

            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isNotNull() },
                { Assertions.assertThat(response.isNew).isFalse() },
            )
        }

        @Test
        fun `랜덤 이름으로 회원가입 한다`() {
            // given
            val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null, null)

            // when
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
            val response = objectMapper.convertValue(result, LoginResponse::class.java)

            // then
            assertAll(
                { Assertions.assertThat(response.tokenResponse.accessToken).isNotNull },
                { Assertions.assertThat(response.tokenResponse.refreshToken).isNotNull },
                { Assertions.assertThat(response.user.id).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.profile).isNotNull() },
                { Assertions.assertThat(response.user.name).isNotNull() },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }
    }

    @Test
    fun `로그아웃을 한다`() {
        // given
        val tokenResponse = loginUser()

        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${tokenResponse.refreshToken}")
            .`when`().post("/api/v1/auth/logout")
            .then().log().all()
            .statusCode(204)
    }

    @Test
    fun `로그아웃 후 리프레시 토큰을 재사용한다`() {
        // given
        val tokenResponse = loginUser()

        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${tokenResponse.refreshToken}")
            .`when`().post("/api/v1/auth/logout")
            .then().log().all()
            .statusCode(204)

        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${tokenResponse.refreshToken}")
            .`when`().post("/api/v1/auth/refresh")
            .then().log().all()
            .statusCode(401)
    }

    @Test
    fun `accessToken을 재발급 한다`() {
        val tokenResponse = loginUser()

        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer ${tokenResponse.refreshToken}")
                .`when`().post("/api/v1/auth/refresh")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        val response = objectMapper.convertValue(result, TokenResponse::class.java)

        // then
        assertAll(
            { Assertions.assertThat(response.accessToken).isNotNull },
            { Assertions.assertThat(response.refreshToken).isNotNull },
        )
    }

    private fun loginUser(): TokenResponse {
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
        return loginResponse.tokenResponse
    }
}
