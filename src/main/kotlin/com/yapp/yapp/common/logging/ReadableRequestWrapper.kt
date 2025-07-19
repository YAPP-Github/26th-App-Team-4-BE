package com.yapp.yapp.common.logging

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.Part
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class ReadableRequestWrapper(
    request: HttpServletRequest,
) : HttpServletRequestWrapper(request) {
    private val encoding: Charset
    private val parts: Collection<Part>?
    val contentAsByteArray: ByteArray

    init {
        this.encoding = getEncoding(request.characterEncoding)
        this.parts = getMultipartParts(request)

        try {
            this.contentAsByteArray = request.inputStream.readAllBytes()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getEncoding(charEncoding: String): Charset {
        if (charEncoding.isNullOrBlank()) {
            return StandardCharsets.UTF_8
        }
        return Charset.forName(charEncoding)
    }

    private fun getMultipartParts(request: HttpServletRequest): Collection<Part>? {
        if (isMultipartRequest(request)) {
            return request.parts
        }
        return null
    }

    private fun isMultipartRequest(request: HttpServletRequest): Boolean {
        return !request.contentType.isNullOrBlank() && request.contentType.startsWith(MULTIPART_FORM_DATA_VALUE)
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(this.contentAsByteArray)

        return object : ServletInputStream() {
            override fun isFinished(): Boolean = throw UnsupportedOperationException("[ReadableRequestWrapper] isFinished() not supported")

            override fun isReady(): Boolean = throw UnsupportedOperationException("[ReadableRequestWrapper] isReady() not supported")

            override fun setReadListener(listener: ReadListener) = Unit

            override fun read(): Int = byteArrayInputStream.read()
        }
    }
}
