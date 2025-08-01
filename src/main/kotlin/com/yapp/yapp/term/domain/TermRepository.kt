package com.yapp.yapp.term.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TermRepository : JpaRepository<Term, Long> {
    fun findByTermType(termType: TermType): Term?
}
