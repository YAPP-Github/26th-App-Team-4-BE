package com.yapp.yapp.user.domain.onbording

import com.yapp.yapp.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Onboarding(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val QuestionType: OnboardingQuestionType,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val answer: AnswerLabel,
    @Column(nullable = false)
    val isDeleted: Boolean = false,
)
