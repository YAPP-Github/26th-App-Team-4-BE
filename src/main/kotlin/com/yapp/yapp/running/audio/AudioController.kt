package com.yapp.yapp.running.audio

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/audios")
class AudioController(
    private val audioService: AudioService,
) {
    @GetMapping("/**")
    fun streamAudio(
        request: HttpServletRequest,
        @RequestHeader(value = "Range", required = false) rangeHeader: String?,
    ): ResponseEntity<Resource> {
        val filename = AudioRequestHandler.parseFilename(request)
        val (blob, resource) = audioService.loadBlob(filename)
        return AudioRequestHandler.handle(blob, resource, rangeHeader)
    }
}
