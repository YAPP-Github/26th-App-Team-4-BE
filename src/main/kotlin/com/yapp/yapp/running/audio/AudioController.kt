package com.yapp.yapp.running.audio

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.CacheControl
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.channels.Channels
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/audios")
class AudioController(
    private val audioService: AudioService,
) {
    @GetMapping("/**")
    fun streamAudio(
        request: HttpServletRequest,
        @RequestHeader(value = "Range", required = false) rangeHeader: String?,
    ): ResponseEntity<Resource> {
        // 1) URL 에서 실제 파일 경로 추출
        val filename = AudioRequestHandler.parseFilename(request)

        val (blob, resource) =
            audioService.loadBlob(filename)
                ?: return ResponseEntity.notFound().build()

        val totalSize = blob.size
        val contentType = MediaType.parseMediaType(blob.contentType ?: "application/octet-stream")

        // Range 미지정 시 전체 전송
        if (rangeHeader == null) {
            val headers =
                HttpHeaders().apply {
                    this.contentType = contentType
                    contentLength = totalSize
                    cacheControl = CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().toString()
                }
            return ResponseEntity.ok().headers(headers).body(resource)
        }

        // Range 요청 처리 (파싱 로직 동일)
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

    // "bytes=start-end" 포맷의 Range 헤더를 파싱하고 파일 크기에 맞춰 보정
    private fun parseRange(
        header: String,
        total: Long,
    ): Pair<Long, Long> {
        val matcher =
            Regex("""bytes=(\d*)-(\d*)""").find(header)
                ?: throw IllegalArgumentException("Invalid Range header: $header")
        val (s, e) = matcher.destructured
        val start = s.toLongOrNull() ?: 0L
        val end = e.toLongOrNull() ?: (total - 1)
        return start.coerceAtLeast(0) to end.coerceAtMost(total - 1)
    }
}
