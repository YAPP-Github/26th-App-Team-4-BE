package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.snippet.Snippet
import java.util.LinkedList
import java.util.List

class RestDocsResponse(
    private val resourceBuilder: ResourceSnippetParametersBuilder,
    private val snippets: MutableList<Snippet?> = LinkedList<Snippet?>(),
) {
    fun setSchema(name: String): RestDocsResponse {
        resourceBuilder.responseSchema(Schema.schema(name))
        return this
    }

    fun responseHeader(vararg descriptors: HeaderDescriptor): RestDocsResponse {
        resourceBuilder.responseHeaders(*descriptors)
        snippets.add(responseHeaders(*descriptors))
        return this
    }

    fun responseBodyField(vararg descriptors: FieldDescriptor): RestDocsResponse {
        val baseFields = getBaseFiled()
        resourceBuilder.responseFields(*descriptors, *baseFields.toTypedArray())
        snippets.add(responseFields(*descriptors, *baseFields.toTypedArray()))
        return this
    }

    fun responseBodyFieldWithResult(vararg descriptors: FieldDescriptor): RestDocsResponse {
        val baseFields = getBaseFieldWithResult()
        resourceBuilder.responseFields(*descriptors, *baseFields.toTypedArray())
        snippets.add(responseFields(*descriptors, *baseFields.toTypedArray()))
        return this
    }

    fun responseBodyFieldWithError(vararg descriptors: FieldDescriptor): RestDocsResponse {
        resourceBuilder.responseFields(*descriptors)
        snippets.add(responseFields(*descriptors))
        return this
    }

    fun getSnippets(): MutableList<Snippet?> {
        return List.copyOf<Snippet?>(snippets)
    }

    fun toSnippets(): Array<Snippet> {
        return snippets.filterNotNull().toTypedArray()
    }

    private fun getBaseFiled(): MutableList<FieldDescriptor> {
        return mutableListOf(
            fieldWithPath("code").description("응답 코드"),
            fieldWithPath("timeStamp").description("응답 시간"),
        )
    }

    private fun getBaseFieldWithResult(): MutableList<FieldDescriptor> {
        return mutableListOf(
            fieldWithPath("code").description("응답 코드"),
            fieldWithPath("timeStamp").description("응답 시간"),
            fieldWithPath("result").description("응답 객체 (응답 값이 없는 경우 result는 없습니다)").optional(),
        )
    }
}
