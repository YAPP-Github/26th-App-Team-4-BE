package com.yapp.yapp

import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.domain.AuthService
import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.auth.infrastructure.provider.kakao.KakaoFeignClient
import com.yapp.yapp.common.web.ApiResponse
import com.yapp.yapp.support.BaseControllerTest
import com.yapp.yapp.support.fixture.IdTokenFixture
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

// @Import(LoggingDataSourceConfig::class)
class ConnectionTest : BaseControllerTest() {
    @MockitoBean
    lateinit var kakaoFeignClient: KakaoFeignClient

    @MockitoBean
    lateinit var appleFeignClient: AppleFeignClient

    @Autowired
    lateinit var testService: TestService

    @Autowired
    lateinit var authService: AuthService

    @Test
    fun `로그인 API 커넥션 테스트`() {
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

        // when

        // then
    }

    @Test
    fun `확인 테스트`() {
        // given
        val callCount = 2
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(callCount)
        val exceptionCount = AtomicInteger(0)
        val successCount = AtomicInteger(0)
        val executor = Executors.newScheduledThreadPool(callCount)

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

        // when
        repeat(callCount) {
            executor.submit {
                startLatch.await() // 동시에 시작
                try {
                    authService.login(ProviderType.TEST, loginRequest)
                    successCount.incrementAndGet()
                } catch (_: Exception) {
                    // 실패는 세지 않음 필요하면 여기서 로그 처리 또는 fail 처리
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        startLatch.countDown()
        val finished = doneLatch.await(10, TimeUnit.SECONDS)
        executor.shutdown()

        Assertions.assertThat(finished).isTrue()
        Assertions.assertThat(successCount.get()).isEqualTo(callCount)
    }
}
