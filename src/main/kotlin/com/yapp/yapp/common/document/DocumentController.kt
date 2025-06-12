package com.yapp.yapp.common.document

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DocumentController {
    @GetMapping("/api/docs")
    fun getDocument(): String {
        return "redirect:/static/dist/index.html"
    }
}
