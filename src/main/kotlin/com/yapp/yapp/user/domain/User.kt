package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "USERS")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, unique = true)
    val nickname: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: ProviderType,
    @Enumerated(EnumType.STRING)
    var runnerType: RunnerType? = null,
    @Column(nullable = false)
    var isDeleted: Boolean = false,
) {
    fun getRunnerType(): RunnerType {
        return runnerType
            ?: throw CustomException(ErrorCode.RUNNER_TYPE_NOT_FOUND)
    }
}
