package com.yapp.yapp.common.web

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "response") // XML 루트 엘리먼트 이름
data class ApiXmlResponse<T>(
    @JacksonXmlProperty(localName = "code")
    val code: String,
    @JacksonXmlProperty(localName = "message")
    val message: String,
    @JacksonXmlProperty(localName = "data")
    val result: T,
) {
    companion object {
        fun <T> success(data: T): ApiXmlResponse<T> = ApiXmlResponse(code = "200", message = "OK", result = data)
    }
}
