package com.yapp.yapp.support

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(controllers = [BaseEntityTest::class])
abstract class BaseEntityTest
