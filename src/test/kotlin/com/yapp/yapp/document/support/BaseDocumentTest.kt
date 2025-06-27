package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseDocumentTest {
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

    @Autowired
    lateinit var objectMapper: ObjectMapper

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
}
