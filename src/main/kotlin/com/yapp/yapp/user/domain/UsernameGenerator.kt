package com.yapp.yapp.user.domain

import java.security.MessageDigest
import java.util.Base64

object UsernameGenerator {
    private const val ALGORITHM = "SHA-256"

    private val adjectives =
        listOf(
            "빠른", "센", "긴", "짧은", "큰",
            "작은", "높은", "낮은", "굳은", "강한",
        )

    private val nouns =
        listOf(
            "러너스", "스프린터", "조깅러", "트레일러", "챌린저",
            "페이스", "도전자", "마라톤", "피니셔", "거리왕",
        )

    fun generate(email: String): String {
        val hash = MessageDigest.getInstance(ALGORITHM).digest(email.toByteArray(Charsets.UTF_8))
        val adjective = adjectives[hash[0].toUByte().toInt() % adjectives.size]
        val noun = nouns[hash[1].toUByte().toInt() % nouns.size]

        val hashSuffix =
            Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hash.copyOfRange(2, 6))

        return "$adjective$noun$hashSuffix".take(10)
    }
}
