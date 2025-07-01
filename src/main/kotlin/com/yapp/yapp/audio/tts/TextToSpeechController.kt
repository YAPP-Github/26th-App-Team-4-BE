package com.yapp.yapp.audio.tts

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tts")
class TextToSpeechController(
    private val ttsService: TextToSpeechService,
) {
    @PostMapping
    fun synthesize(
        @RequestParam text: String,
    ): ResponseEntity<ByteArrayResource> {
        val audioBytes = ttsService.synthesize(text)
        val resource = ByteArrayResource(audioBytes)

        val headers =
            HttpHeaders().apply {
                contentType = MediaType.parseMediaType("audio/mpeg")
                add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"speech.mp3\"")
            }

        return ResponseEntity.ok()
            .headers(headers)
            .body(resource)
    }
}
