package com.yapp.yapp.audio.tts

import com.yapp.yapp.audio.domain.AudioServletHandler
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tts")
class TextToSpeechController(
    private val ttsManager: TextToSpeechManager,
) {
    @PostMapping
    fun synthesize(
        @RequestParam text: String,
    ): ResponseEntity<ByteArrayResource> {
        val audioBytes = ttsManager.synthesize(text)
        val resource = ByteArrayResource(audioBytes)

        return ResponseEntity.ok()
            .headers(AudioServletHandler.createAudioHeader())
            .body(resource)
    }
}
