package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): User?

    fun findByEmailAndProvider(
        email: String,
        provider: ProviderType,
    ): User?

    fun findByEmail(email: String): User?
}
