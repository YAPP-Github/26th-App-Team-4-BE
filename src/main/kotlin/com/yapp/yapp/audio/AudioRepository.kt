package com.yapp.yapp.audio

interface AudioRepository {
    fun getRandomCoachAudio(): AudioResource

    fun loadResource(filePath: String): AudioResource
}
