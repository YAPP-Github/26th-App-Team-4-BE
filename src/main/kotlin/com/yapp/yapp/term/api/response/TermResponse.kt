package com.yapp.yapp.term.api.response

import com.yapp.yapp.term.domain.Term

data class TermResponse(
    val title: String,
    val content: String,
    val isRequired: Boolean,
) {
    constructor(term: Term) : this(
        title = term.title,
        content = term.content,
        isRequired = term.isRequired,
    )
}
