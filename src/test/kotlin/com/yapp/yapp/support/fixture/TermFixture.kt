package com.yapp.yapp.support.fixture

import com.yapp.yapp.term.domain.Term
import com.yapp.yapp.term.domain.TermRepository
import com.yapp.yapp.term.domain.TermType
import org.springframework.stereotype.Component

@Component
class TermFixture(
    private val termRepository: TermRepository,
) {
    fun create(
        title: String = "개인 정보 처리 방침",
        termType: TermType = TermType.PRIVATE_POLICY,
        content: String =
            """
            개인정보처리방침 정책
            ...
            ...
            ...
            개인정보처리방침 정책
            개인정보처리방침 정책
            개인정보처리방침 정책
            개인정보처리방침 정책
            개인정보처리방침 정책
            개인정보처리방침 정책
            ...
            """.trimIndent(),
        isRequired: Boolean = true,
    ): Term {
        return termRepository.save(
            Term(
                termType = termType,
                title = title,
                content = content,
                isRequired = isRequired,
            ),
        )
    }
}
