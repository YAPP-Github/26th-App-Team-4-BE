package com.yapp.yapp.term.domain

import com.yapp.yapp.term.api.response.TermResponse
import org.springframework.stereotype.Service

@Service
class TermService(
    private val termManager: TermManager,
) {
    fun getTermByTermType(termType: TermType): TermResponse {
        val term = termManager.getTermByTermType(termType)
        return TermResponse(term)
    }
}
