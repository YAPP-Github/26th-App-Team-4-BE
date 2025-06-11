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

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseDocumentTest {
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
