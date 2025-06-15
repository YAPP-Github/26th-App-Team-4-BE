package com.yapp.yapp.common.token.oidc

class OidcProperties(
    val keySet: String,
    val issuer: String,
    val clientId: String,
) {
    var nonce: String? = null
        set(value) {
            field = value
        }
}
