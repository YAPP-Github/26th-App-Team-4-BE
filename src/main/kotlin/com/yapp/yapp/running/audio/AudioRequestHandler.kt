package com.yapp.yapp.running.audio

import com.google.cloud.storage.Blob
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.net.URLDecoder
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object AudioRequestHandler {
    fun parseFilename(request: HttpServletRequest): String {
        val rawPath = request.requestURI
        return URLDecoder.decode(
            rawPath.removePrefix("/api/v1/audios/"),
            StandardCharsets.UTF_8,
        )
    }

    fun handle(
        blob: Blob,
        resource: Resource,
        rangeHeader: String?,
    ): ResponseEntity<Resource> {
        return if (rangeHeader != null) {
            handleRangeHeader(blob, rangeHeader)
        } else {
            handleNoRangeHeader(blob, resource)
        }
    }

    fun handleRangeHeader(
        blob: Blob,
        rangeHeader: String,
    ): ResponseEntity<Resource> {
        val totalSize = blob.size
        val contentType = MediaType.parseMediaType(blob.contentType ?: "application/octet-stream")

        val (start, end) = parseRange(rangeHeader, totalSize)
        val chunkSize = end - start + 1
        val rangedStream = Channels.newInputStream(blob.reader().apply { seek(start) })
        val rangedResource = InputStreamResource(rangedStream)

        val headers =
            HttpHeaders().apply {
                this.contentType = contentType
                contentLength = chunkSize
                add(HttpHeaders.ACCEPT_RANGES, "bytes")
                add(HttpHeaders.CONTENT_RANGE, "bytes $start-$end/$totalSize")
                cacheControl = CacheControl.noCache().toString()
            }
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
            .headers(headers)
            .body(rangedResource)
    }

    private fun handleNoRangeHeader(
        blob: Blob,
        resource: Resource,
    ): ResponseEntity<Resource> {
        val totalSize = blob.size
        val contentType = MediaType.parseMediaType(blob.contentType ?: "application/octet-stream")

        val headers =
            HttpHeaders().apply {
                this.contentType = contentType
                contentLength = totalSize
                cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().toString()
            }
        return ResponseEntity.ok().headers(headers)
            .body(resource)
    }

    private fun parseRange(
        header: String,
        total: Long,
    ): Pair<Long, Long> {
        val matcher =
            Regex("""bytes=(\d*)-(\d*)""").find(header)
                ?: throw CustomException(ErrorCode.INVALID_HEADER_REQUEST)
        val (s, e) = matcher.destructured
        val start = s.toLongOrNull() ?: 0L
        val end = e.toLongOrNull() ?: (total - 1)
        return start.coerceAtLeast(0) to end.coerceAtMost(total - 1)
    }
}
