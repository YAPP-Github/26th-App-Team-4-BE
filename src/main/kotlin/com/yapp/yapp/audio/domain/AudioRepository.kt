package com.yapp.yapp.audio.domain

interface AudioRepository {
    fun getRandomCoachAudio(): AudioResource

    fun loadResource(filePath: String): AudioResource

    fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource

    fun getTimeGoalAudio(type: TimeAudioType): AudioResource

    fun getPaceGoalAudio(type: PaceAudioType): AudioResource
}
