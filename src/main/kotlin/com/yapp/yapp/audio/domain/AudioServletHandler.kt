package com.yapp.yapp.audio.domain

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object AudioServletHandler {
    fun parseFilename(request: HttpServletRequest): String {
        val rawPath = request.requestURI
        return URLDecoder.decode(
            rawPath.removePrefix("/api/v1/audios/"),
            StandardCharsets.UTF_8,
        )
    }

    fun handleAudioResource(audioResource: AudioResource): ResponseEntity<Resource> {
        val headers =
            HttpHeaders().apply {
                this.contentType = audioResource.contentType ?: MediaType.APPLICATION_OCTET_STREAM
                contentLength = audioResource.totalSize
                cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().toString()
            }
        return ResponseEntity.ok().headers(headers)
            .body(audioResource.resource)
    }

    fun handleAudioResource(audioBytes: ByteArray): ResponseEntity<ByteArrayResource> {
        val resource = ByteArrayResource(audioBytes)
        val headers = createAudioHeader()
        return ResponseEntity.ok()
            .headers(headers)
            .body(resource)
    }

    fun createAudioHeader(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.parseMediaType("audio/mpeg")
            add(HttpHeaders.CONTENT_DISPOSITION, "inline")
        }
    }
}
