package com.yapp.yapp.common.logging

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingWrappingFilter : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        val wrappedRequest =
            ContentCachingRequestWrapper(request as HttpServletRequest).apply {
                setCharacterEncoding("UTF-8")
            }
        val wrappedResponse =
            ContentCachingResponseWrapper(response as HttpServletResponse).apply {
                characterEncoding = "UTF-8"
            }

        try {
            chain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            wrappedResponse.copyBodyToResponse()
        }
    }
}
