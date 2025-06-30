package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.yapp.yapp.auth.api.request.LoginRequest
import com.yapp.yapp.auth.api.response.LoginResponse
import com.yapp.yapp.auth.api.response.accessToken
import com.yapp.yapp.auth.infrastructure.provider.apple.AppleFeignClient
import com.yapp.yapp.common.ApiResponse
import com.yapp.yapp.support.BaseSupportMethod
import com.yapp.yapp.support.DatabaseCleanerExtension
import com.yapp.yapp.support.fixture.IdTokenFixture
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ExtendWith(RestDocumentationExtension::class, DatabaseCleanerExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseDocumentTest : BaseSupportMethod() {
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

    @LocalServerPort
    private var port: Int = 0
    protected var spec: RequestSpecification? = null
    lateinit var resourceBuilder: ResourceSnippetParametersBuilder

    @MockitoBean
    private lateinit var feignClient: AppleFeignClient

    @BeforeEach
    fun setEnvironment(restDocumentation: RestDocumentationContextProvider?) {
        RestAssured.port = port
        val webConfigurer =
            RestAssuredRestDocumentation.documentationConfiguration(restDocumentation)
        spec =
            RequestSpecBuilder()
                .addFilter(webConfigurer)
                .build()
        resourceBuilder = ResourceSnippetParametersBuilder()
    }

    protected fun request(): RestDocsRequest = RestDocsRequest(resourceBuilder)

    protected fun response(): RestDocsResponse = RestDocsResponse(resourceBuilder)

    protected fun filter(
        identifierPrefix: String,
        identifier: String,
    ): RestDocsFilterBuilder = RestDocsFilterBuilder(resourceBuilder, identifierPrefix, identifier)

    protected fun getAccessToken(email: String = "test@test.com"): String {
        val loginResponse = loginUser(email)
        return "Bearer ${loginResponse.accessToken()}"
    }

    private fun loginUser(email: String): LoginResponse {
        val idToken = IdTokenFixture.createValidIdToken(issuer = "https://appleid.apple.com", email = email)
        val jwksResponse = IdTokenFixture.createPublicKeyResponse()
        val loginRequest = LoginRequest(idToken, null, null)

        Mockito.`when`(feignClient.fetchJwks())
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

        return objectMapper.convertValue(result, LoginResponse::class.java)
    }
}
