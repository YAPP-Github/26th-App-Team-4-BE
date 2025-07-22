package com.yapp.yapp.audio

interface AudioRepository {
    fun getRandomCoachAudio(): AudioResource

    fun loadResource(filePath: String): AudioResource

    fun getDistanceGoalAudio(type: DistanceAudioType): AudioResource

    fun getTimeGoalAudio(type: TimeAudioType): AudioResource

    fun getPaceAudio(type: PaceAudioType): AudioResource
}
