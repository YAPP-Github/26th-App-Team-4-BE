package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import com.yapp.yapp.document.Tag
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestDocumentationFilter
import org.springframework.restdocs.snippet.Snippet

class RestDocsFilterBuilder private constructor(
    private var resourceBuilder: ResourceSnippetParametersBuilder,
    private val identifier: String,
) {
    private val snippets: MutableList<Snippet> = mutableListOf()

    companion object {
        private const val IDENTIFIER_DELIMITER = "/"

        private val REQUEST_PREPROCESSOR: OperationRequestPreprocessor =
            Preprocessors.preprocessRequest(
                Preprocessors.prettyPrint(),
                Preprocessors.modifyHeaders()
                    .remove(HttpHeaders.HOST)
                    .remove(HttpHeaders.CONTENT_LENGTH),
            )

        private val RESPONSE_PREPROCESSOR: OperationResponsePreprocessor =
            Preprocessors.preprocessResponse(
                Preprocessors.prettyPrint(),
                Preprocessors.modifyHeaders()
                    .remove(HttpHeaders.TRANSFER_ENCODING)
                    .remove(HttpHeaders.DATE)
                    .remove(HttpHeaders.CONNECTION)
                    .remove(HttpHeaders.CONTENT_LENGTH),
            )
    }

    constructor(resourceBuilder: ResourceSnippetParametersBuilder, identifierPrefix: String, identifier: String) :
        this(resourceBuilder, "$identifierPrefix$IDENTIFIER_DELIMITER$identifier")

    fun tag(tag: Tag): RestDocsFilterBuilder {
        resourceBuilder.tag(tag.getDisplayName())
        return this
    }

    fun summary(summary: String?): RestDocsFilterBuilder {
        resourceBuilder.summary(summary)
        return this
    }

    fun description(description: String?): RestDocsFilterBuilder {
        resourceBuilder.description(description)
        return this
    }

    fun request(request: RestDocsRequest): RestDocsFilterBuilder {
        snippets.addAll(request.getSnippets().filterNotNull())
        return this
    }

    fun response(response: RestDocsResponse): RestDocsFilterBuilder {
        snippets.addAll(response.getSnippets().filterNotNull())
        return this
    }

    fun build(): RestDocumentationFilter {
        val resourceSnippet = resource(resourceBuilder.build())
        return document(
            identifier,
            REQUEST_PREPROCESSOR,
            RESPONSE_PREPROCESSOR,
            *(listOf(resourceSnippet) + snippets).toTypedArray(),
        )
    }
}
