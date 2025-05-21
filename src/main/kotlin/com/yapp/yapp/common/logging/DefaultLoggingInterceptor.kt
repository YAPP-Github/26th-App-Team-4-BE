package com.yapp.yapp.common.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.Serializable

@Component
class DefaultLoggingInterceptor : BaseLoggingInterceptor() {
    companion object {
        private val logger = KotlinLogging.logger {}
        const val IGNORE_URI = "/health"
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        if (shouldIgnore(request)) return

        val requestId = MDC.get(REQUEST_ID) ?: "N/A"
        val originalUri =
            (request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String)
                ?: request.requestURI
        val duration = calculateDuration()
        val statusText = getStatusText(response.status)
        val headers = extractHeaders(request)
        val body = extractRequestBody(request)

        logger.info {
            """
            📦 RESPONSE $statusText [$requestId]
            ▶ URI: ${request.method} ${originalUri}${getRequestParameters(request)}
            ▶ Status: [${response.status}]
            ▶ Duration: ${duration}ms
            ▶ Headers: $headers
            ▶ Body: $body
            ▶ Exception: ${ex?.message}
            """.trimIndent()
        }
    }

    private fun shouldIgnore(request: HttpServletRequest): Boolean {
        return request.method.equals("OPTIONS", ignoreCase = true) ||
            request.requestURI.contains(IGNORE_URI)
    }

    private fun calculateDuration(): Long {
        val startTime = MDC.get(REQUEST_TIME)?.toLongOrNull() ?: return -1
        return System.currentTimeMillis() - startTime
    }

    private fun getStatusText(statusCode: Int): String {
        return when (statusCode / 100) {
            2, 3 -> "SUCCESS ✅"
            else -> "FAIL ❌"
        }
    }

    private fun extractHeaders(request: HttpServletRequest): Map<String, String> {
        return request.headerNames?.toList()
            ?.associateWith { request.getHeader(it) }
            ?: emptyMap()
    }

    private fun extractRequestBody(request: HttpServletRequest): Serializable {
        return if (request is ContentCachingRequestWrapper) {
            try {
                java.lang.String(request.contentAsByteArray, request.characterEncoding ?: "UTF-8")
            } catch (e: Exception) {
                "[Body 조회를 실패했습니다: ${e.message}]"
            }
        } else {
            "[Request가 ContentCachingRequestWrapper로 변환되지 않았습니다]"
        }
    }

    private fun getRequestParameters(request: HttpServletRequest): String {
        val params = request.parameterNames.toList()
        return if (params.isEmpty()) "" else "?" + params.joinToString("&") { "$it=${request.getParameter(it)}" }
    }
}
