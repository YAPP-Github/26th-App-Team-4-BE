package com.yapp.yapp.audio.api

import com.yapp.yapp.audio.domain.AudioService
import com.yapp.yapp.audio.domain.AudioServletHandler
import com.yapp.yapp.audio.domain.DistanceAudioType
import com.yapp.yapp.audio.domain.PaceAudioType
import com.yapp.yapp.audio.domain.TimeAudioType
import com.yapp.yapp.common.token.jwt.annotation.CurrentUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/audios")
class AudioController(
    private val audioService: AudioService,
) {
    @GetMapping("/**")
    fun streamAudio(request: HttpServletRequest): ResponseEntity<Resource> {
        val filename = AudioServletHandler.parseFilename(request)
        val audioResource = audioService.loadResource(filename)
        return AudioServletHandler.handleAudioResource(audioResource)
    }

    @GetMapping("/coach")
    fun getCoachAudio(): ResponseEntity<Resource> {
        val audioResource = audioService.getCoachAudio()
        return AudioServletHandler.handleAudioResource(audioResource)
    }

    @GetMapping("/running-info")
    fun getRunningInfoAudio(
        @CurrentUser userId: Long,
    ) {
    }

    @GetMapping("/goals/pace")
    fun getPaceAudio(
        @RequestParam type: String,
    ): ResponseEntity<Resource> {
        val audioResource = audioService.getPaceGoalAudio(PaceAudioType.Companion.getByName(type))
        return AudioServletHandler.handleAudioResource(audioResource)
    }

    @GetMapping("/goals/distance")
    fun getDistanceGoalAudio(
        @RequestParam type: String,
    ): ResponseEntity<Resource> {
        val audioResource = audioService.getDistanceGoalAudio(DistanceAudioType.Companion.getByName(type))
        return AudioServletHandler.handleAudioResource(audioResource)
    }

    @GetMapping("/goals/time")
    fun getTimeGoalAudio(
        @RequestParam type: String,
    ): ResponseEntity<Resource> {
        val audioResource = audioService.getTimeGoalAudio(TimeAudioType.Companion.getByName(type))
        return AudioServletHandler.handleAudioResource(audioResource)
    }
}
