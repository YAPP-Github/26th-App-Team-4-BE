package com.yapp.yapp.common.token.oidc

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class OidcProperties
    @JsonCreator
    constructor(
        @JsonProperty("keySet") val keySet: String,
        @JsonProperty("issuer") val issuer: String,
        @JsonProperty("clientId") val clientId: List<String>,
    ) {
        var nonce: String? = null
            set(value) {
                field = value
            }
    }
