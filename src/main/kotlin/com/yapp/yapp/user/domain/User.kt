package com.yapp.yapp.user.domain

import com.yapp.yapp.auth.infrastructure.provider.ProviderType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email", "provider"]),
    ],
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column
    var name: String,
    @Column(nullable = false)
    var email: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var provider: ProviderType,
    @Column(nullable = false)
    var profileImage: String,
    @Column(nullable = false)
    var isDeleted: Boolean = false,
)
