package com.yapp.yapp.auth.infrastructure.provider

import com.yapp.yapp.common.token.oidc.OidcProperties

interface ApiClient {
    fun getOidcProperties(): OidcProperties
}
