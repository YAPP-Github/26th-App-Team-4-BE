package com.yapp.yapp.auth.api.controller

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoFeignClient
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
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
            val idToken =
                IdTokenFixture.createValidIdToken(
                    issuer = "https://kauth.kakao.com",
                    audience = "test-app-key-kakao",
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null)

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
                { Assertions.assertThat(response.user.userId).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.nickname).isNotNull() },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `애플 로그인`() {
            // given
            val idToken =
                IdTokenFixture.createValidIdToken(
                    issuer = "https://appleid.apple.com",
                    audience = "test-app-key-apple",
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, null)

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
                { Assertions.assertThat(response.user.userId).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.nickname).isNotNull() },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `카카오 로그인 with nonce`() {
            // given
            val nonce = UUID.randomUUID().toString()
            val idToken =
                IdTokenFixture.createValidIdToken(
                    issuer = "https://kauth.kakao.com",
                    audience = "test-app-key-kakao",
                    nonce = nonce,
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, nonce)

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
                { Assertions.assertThat(response.user.userId).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.nickname).isNotNull() },
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
                    audience = "test-app-key-apple",
                    nonce = nonce,
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest = LoginRequest(idToken, nonce)

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
                { Assertions.assertThat(response.user.userId).isNotNull() },
                { Assertions.assertThat(response.user.email).isNotNull() },
                { Assertions.assertThat(response.user.nickname).isNotNull() },
                { Assertions.assertThat(response.isNew).isTrue() },
            )
        }

        @Test
        fun `애플 로그인 다중 키를 지원한다`() {
            // given
            val idToken1 =
                IdTokenFixture.createValidIdToken(
                    email = "test@app.com",
                    issuer = "https://appleid.apple.com",
                    audience = "test-app-key-apple",
                )
            val idToken2 =
                IdTokenFixture.createValidIdToken(
                    email = "test@rest.com",
                    issuer = "https://appleid.apple.com",
                    audience = "test-rest-key-apple",
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest1 = LoginRequest(idToken1, null)
            val loginRequest2 = LoginRequest(idToken2, null)

            // when
            // then
            Mockito.`when`(appleFeignClient.fetchJwks())
                .thenReturn(objectMapper.writeValueAsString(jwksResponse))

            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest1)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)

            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest2)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
        }

        @Test
        fun `카카오 로그인 다중 키를 지원한다`() {
            // given
            val idToken1 =
                IdTokenFixture.createValidIdToken(
                    email = "test@app.com",
                    issuer = "https://kauth.kakao.com",
                    audience = "test-app-key-kakao",
                )
            val idToken2 =
                IdTokenFixture.createValidIdToken(
                    email = "test@rest.com",
                    issuer = "https://kauth.kakao.com",
                    audience = "test-rest-key-kakao",
                )
            val jwksResponse = IdTokenFixture.createPublicKeyResponse()
            val loginRequest1 = LoginRequest(idToken1, null)
            val loginRequest2 = LoginRequest(idToken2, null)

            // when
            // then
            Mockito.`when`(kakaoFeignClient.fetchJwks())
                .thenReturn(objectMapper.writeValueAsString(jwksResponse))

            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest1)
                .`when`().post("/api/v1/auth/login/kakao")
                .then().log().all()
                .statusCode(200)

            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(loginRequest2)
                .`when`().post("/api/v1/auth/login/kakao")
                .then().log().all()
                .statusCode(200)
        }
    }

    @Test
    fun `회원가입 후 로그인`() {
        // given
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com")
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null)

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
            { Assertions.assertThat(response.user.userId).isNotNull() },
            { Assertions.assertThat(response.user.email).isNotNull() },
            { Assertions.assertThat(response.user.nickname).isNotNull() },
            { Assertions.assertThat(response.isNew).isFalse() },
        )
    }

    @Test
    fun `같은 이메일로 서로 다른 소셜 로그인을 시도 한다`() {
        // given
        val appleIdToken =
            IdTokenFixture.createValidIdToken(
                email = "dup@dup.com",
                issuer = "https://appleid.apple.com",
            )
        val appleLoginRequest = LoginRequest(appleIdToken, null)

        val kakaoIdToken =
            IdTokenFixture.createValidIdToken(
                email = "dup@dup.com",
                issuer = "https://kauth.kakao.com",
                audience = "test-app-key-kakao",
            )
        val kakaoLoginRequest = LoginRequest(kakaoIdToken, null)

        val jwksResponse = IdTokenFixture.createPublicKeyResponse()

        // when
        Mockito.`when`(appleFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        Mockito.`when`(kakaoFeignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        val appleResult =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(appleLoginRequest)
                .`when`().post("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result
        val appleResponse = objectMapper.convertValue(appleResult, LoginResponse::class.java)

        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(kakaoLoginRequest)
            .`when`().post("/api/v1/auth/login/kakao")
            .then().log().all()
            .statusCode(400)

        assertAll(
            { Assertions.assertThat(appleResponse.tokenResponse.accessToken).isNotNull },
            { Assertions.assertThat(appleResponse.tokenResponse.refreshToken).isNotNull },
            { Assertions.assertThat(appleResponse.user.userId).isNotNull() },
            { Assertions.assertThat(appleResponse.user.email).isNotNull() },
            { Assertions.assertThat(appleResponse.user.nickname).isNotNull() },
            { Assertions.assertThat(appleResponse.isNew).isTrue() },
        )
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

    @Test
    fun `로그아웃 후 사용자 정보를 조회한다`() {
        val tokenResponse = loginUser()

        RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .header("Authorization", "Bearer ${tokenResponse.refreshToken}")
            .`when`().post("/api/v1/auth/logout")
            .then().log().all()
            .statusCode(204)

        RestAssured.given().log().all()
            .header("Authorization", "Bearer ${tokenResponse.accessToken}")
            .`when`().get("/api/v1/users")
            .then().log().all()
            .statusCode(401)
    }

    private fun loginUser(): TokenResponse {
        val idToken =
            IdTokenFixture.createValidIdToken(
                issuer = "https://appleid.apple.com",
                audience = "test-app-key-apple",
            )
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null)

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
