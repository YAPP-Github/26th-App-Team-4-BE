package com.deepromeet.atcha.support

import com.deepromeet.atcha.support.fixture.UserFixture
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.yapp.auth.api.response.TokenResponse
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.fixture.IdTokenFixture
import com.yapp.yapp.user.domain.User
import com.yapp.yapp.user.domain.UserRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseControllerTest {
    companion object {
        @Container
        @JvmStatic
        val redisContainer =
            GenericContainer("redis:latest").apply {
                withExposedPorts(6379)
                waitingFor(Wait.forListeningPort())
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun redisProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host") { redisContainer.host }
            registry.add("spring.data.redis.port") { redisContainer.getMappedPort(6379) }
        }
    }

    @Autowired
    lateinit var userRepository: UserRepository

    @MockitoBean
    private lateinit var feignClient: AppleFeignClient

    protected val objectMapper =
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setPort() {
        RestAssured.port = port
    }

    protected fun getAccessToken(email: String = "test@test.com"): String {
        val tokenResponse = loginUser(email)
        return "Bearer ${tokenResponse.accessToken}"
    }

    protected fun getSavedUser(user: User = UserFixture.create()): User {
        return userRepository.save(user)
    }

    private fun loginUser(email: String): TokenResponse {
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com", email = email)
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()

        Mockito.`when`(feignClient.fetchJwks())
            .thenReturn(objectMapper.writeValueAsString(jwksResponse))

        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .param("idToken", idToken)
                .`when`().get("/api/v1/auth/login/apple")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(ApiResponse::class.java)
                .result

        return objectMapper.convertValue(result, TokenResponse::class.java)
    }

    protected fun <T> convert(
        result: Any?,
        classType: Class<T>,
    ): T = objectMapper.convertValue(result, classType)
}
