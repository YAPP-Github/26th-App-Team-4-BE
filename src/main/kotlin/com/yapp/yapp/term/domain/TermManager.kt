package com.yapp.yapp.term.domain

import org.springframework.stereotype.Component

@Component
class TermManager(
    private val termDao: TermDao,
) {
    fun getTermByTermType(termType: TermType) = termDao.getTermByTermType(termType)
}
