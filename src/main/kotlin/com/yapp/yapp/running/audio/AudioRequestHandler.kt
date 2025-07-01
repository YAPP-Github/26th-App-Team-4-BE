package com.yapp.yapp.running.audio

import jakarta.servlet.http.HttpServletRequest
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object AudioRequestHandler {
    fun parseFilename(request: HttpServletRequest): String {
        val rawPath = request.requestURI
        return URLDecoder.decode(
            rawPath.removePrefix("/api/audios/"),
            StandardCharsets.UTF_8,
        )
    }
}
