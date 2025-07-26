package com.yapp.yapp.audio.tts

import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import com.google.protobuf.ByteString
import org.springframework.stereotype.Service

@Service
class TextToSpeechService(
    private val client: TextToSpeechClient,
    private val voiceSelectionParams: VoiceSelectionParams,
    private val audioConfig: AudioConfig,
) {
    fun synthesize(text: String): ByteArray {
        // 1) 입력 내용 설정
        val input =
            SynthesisInput.newBuilder()
                .setText(text)
                .build()

        client.use {
            val response = it.synthesizeSpeech(input, voiceSelectionParams, audioConfig)
            val audioBytes: ByteString = response.audioContent
            return audioBytes.toByteArray()
        }
    }
}
