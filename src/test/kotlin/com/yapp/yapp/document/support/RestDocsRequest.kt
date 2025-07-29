package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.requestPartBody
import org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.restdocs.request.RequestPartDescriptor
import org.springframework.restdocs.snippet.Snippet
import java.util.LinkedList
import java.util.List

class RestDocsRequest(
    private val resourceBuilder: ResourceSnippetParametersBuilder,
    private val snippets: MutableList<Snippet?> = LinkedList<Snippet?>(),
) {
    fun setSchema(name: String): RestDocsRequest {
        resourceBuilder.requestSchema(Schema.schema(name))
        return this
    }

    fun pathParameter(vararg descriptors: ParameterDescriptor): RestDocsRequest {
        resourceBuilder.pathParameters(*descriptors) // resource.json 에 반영
        snippets.add(pathParameters(*descriptors)) // HTML snippet 생성
        return this
    }

    fun queryParameter(vararg descriptors: ParameterDescriptor): RestDocsRequest {
        resourceBuilder.queryParameters(*descriptors) // resource.json 에 반영
        snippets.add(queryParameters(*descriptors)) // HTML snippet 생성
        return this
    }

    fun requestHeader(vararg descriptors: HeaderDescriptor): RestDocsRequest {
        resourceBuilder.requestHeaders(*descriptors) // resource.json 에 반영
        snippets.add(requestHeaders(*descriptors)) // HTML snippet 생성
        return this
    }

    fun requestBodyField(vararg descriptors: FieldDescriptor): RestDocsRequest {
        resourceBuilder.requestFields(*descriptors)
        snippets.add(requestFields(*descriptors))
        return this
    }

    fun requestBodyField(
        multiFileField: String,
        vararg descriptors: FieldDescriptor,
    ): RestDocsRequest {
//        resourceBuilder.requestFields(*descriptors)
        snippets.add(requestPartBody(multiFileField))
        snippets.add(requestPartFields(multiFileField, *descriptors))
        return this
    }

    fun multipartField(vararg descriptors: RequestPartDescriptor): RestDocsRequest {
        snippets.add(requestParts(*descriptors))
        return this
    }

    fun getSnippets(): MutableList<Snippet?> {
        return List.copyOf<Snippet?>(snippets)
    }
}
