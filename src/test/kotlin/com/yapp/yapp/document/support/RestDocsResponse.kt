package com.yapp.yapp.document.support

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.payload.FieldDescriptor
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
}
