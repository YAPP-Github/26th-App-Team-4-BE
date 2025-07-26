package com.yapp.yapp.audio.tts

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SsmlVoiceGender
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.TextToSpeechSettings
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class GcpTextToSpeechConfig(
    private val resourceLoader: ResourceLoader,
    @Value("\${spring.cloud.gcp.credentials.location}") private val credentialsLocation: String,
) {
    @Bean
    fun textToSpeechClient(): TextToSpeechClient {
        val credentials =
            GoogleCredentials.fromStream(
                resourceLoader.getResource(credentialsLocation).inputStream,
            )

        val settings =
            TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build()

        return TextToSpeechClient.create(settings)
    }

    @Bean
    fun voiceSelectParam(): VoiceSelectionParams {
        return VoiceSelectionParams.newBuilder()
            .setLanguageCode("ko-KR")
            .setSsmlGender(SsmlVoiceGender.FEMALE)
            .build()
    }

    @Bean
    fun audioConfig(): AudioConfig {
        return AudioConfig.newBuilder()
            .setAudioEncoding(AudioEncoding.LINEAR16)
            .build()
    }
}
