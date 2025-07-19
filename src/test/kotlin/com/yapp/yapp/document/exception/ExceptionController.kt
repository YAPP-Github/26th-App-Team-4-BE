package com.yapp.yapp.document.exception

import com.yapp.yapp.common.exception.CustomException
import com.yapp.yapp.common.exception.ErrorCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExceptionController {
    @GetMapping("/exception")
    fun exception() {
        throw CustomException(ErrorCode.USER_NOT_FOUND)
    }
}
