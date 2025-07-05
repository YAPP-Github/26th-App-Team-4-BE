package com.yapp.yapp.user.domain

import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

object NicknameGenerator {
    private const val ALGORITHM = "SHA-256"

    private val adjectives =
        listOf(
            "빠른", "든든한", "날쌘", "열정적", "침착한",
            "끈질긴", "강한", "지혜로운", "굳센", "도전적",
            "근성있는", "단단한", "민첩한", "용감한", "성실한",
            "의욕찬", "열의찬", "집중한", "노련한", "끓는",
        )

    private val nouns =
        listOf(
            "러너", "주자", "스프린터", "마라토너", "챌린저",
            "조깅러", "트레일러", "거리왕", "완주자", "도전자",
            "피니셔", "레이서", "기록러", "땀쟁이", "열정가",
            "굳건러", "도약가", "지구력자", "지속러", "페이스러",
        )

    fun generate(email: String): String {
        val salt = UUID.randomUUID().toString()
        val hashInput = "$email:$salt"
        val hash =
            MessageDigest.getInstance(ALGORITHM).digest(hashInput.toByteArray(Charsets.UTF_8))
        val adjective = adjectives[hash[0].toUByte().toInt() % adjectives.size]
        val noun = nouns[hash[1].toUByte().toInt() % nouns.size]

        val hashSuffix =
            Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hash.copyOfRange(2, 6))

        return "$adjective$noun$hashSuffix".take(15)
    }
}
