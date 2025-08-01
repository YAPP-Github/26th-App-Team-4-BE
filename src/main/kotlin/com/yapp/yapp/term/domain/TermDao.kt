package com.yapp.yapp.term.domain

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class TermDao(
    private val termRepository: TermRepository,
) {
    fun getTermByTermType(termType: TermType): Term =
        termRepository.findByTermType(termType)
            ?: throw CustomException(ErrorCode.TERM_NOT_FOUND)
}
