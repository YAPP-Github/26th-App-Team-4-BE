package com.yapp.yapp.support.fixture

import io.jsonwebtoken.Jwts
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID

object IdTokenFixture {
    private val keyPairs: List<Pair<String, KeyPair>> =
        (1..5).map {
            val keyPair =
                KeyPairGenerator.getInstance("RSA").apply {
                    initialize(2048)
                }.generateKeyPair()
            val kid = UUID.randomUUID().toString()
            kid to keyPair
        }

    fun createValidIdToken(
        issuer: String = "https://provider.example.com",
        subject: String = "user123",
        audience: String = "test-client-id",
        email: String = "test@test.com",
        nonce: String? = null,
        expiresInSeconds: Long = 3600,
    ): String {
        val now = Instant.now()
        val (kid, keyPair) = keyPairs.random()
        val jwtBuilder =
            Jwts.builder()
                .header()
                .add("typ", "jwt")
                .add("kid", kid).and()
                .issuer(issuer)
                .subject(subject)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .id(UUID.randomUUID().toString())
                .claim("email", email)

        nonce?.let { jwtBuilder.claim("nonce", it) }

        return jwtBuilder
            .signWith(keyPair.private)
            .compact()
    }

    fun createPublicKeyResponse(): JwksResponse {
        val jwks =
            keyPairs.map { (kid, keyPair) ->
                val publicKey = keyPair.public as RSAPublicKey
                publicKey.toJwk(kid)
            }
        return JwksResponse(keys = jwks)
    }

    private fun RSAPublicKey.toJwk(kid: String): Jwk {
        val encoder = Base64.getUrlEncoder().withoutPadding()
        val modulusBase64 = encoder.encodeToString(this.modulus.toByteArrayUnsigned())
        val exponentBase64 = encoder.encodeToString(this.publicExponent.toByteArrayUnsigned())

        return Jwk(
            kid = kid,
            n = modulusBase64,
            e = exponentBase64,
        )
    }

    private fun BigInteger.toByteArrayUnsigned(): ByteArray {
        val bytes = this.toByteArray()
        return if (bytes[0] == 0.toByte()) bytes.copyOfRange(1, bytes.size) else bytes
    }
}

data class JwksResponse(
    val keys: List<Jwk>,
)

data class Jwk(
    val kid: String,
    val kty: String = "RSA",
    val alg: String = "RS256",
    val use: String = "sig",
    val n: String,
    val e: String,
)
