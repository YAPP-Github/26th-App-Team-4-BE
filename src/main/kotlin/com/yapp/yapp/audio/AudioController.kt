package com.yapp.yapp.audio

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/audios")
class AudioController(
    private val audioService: AudioService,
) {
    @GetMapping("/**")
    fun streamAudio(request: HttpServletRequest): ResponseEntity<Resource> {
        val filename = AudioRequestHandler.parseFilename(request)
        val audioResource = audioService.loadResource(filename)
        return AudioRequestHandler.handle(audioResource)
    }

    @GetMapping("/coach")
    fun getCoachAudio(): ResponseEntity<Resource> {
        val audioResource = audioService.getCoachAudio()
        return AudioRequestHandler.handle(audioResource)
    }
}
