package com.deepromeet.atcha.support

import com.yapp.yapp.support.BaseSupportMethod
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseServiceTest : BaseSupportMethod()
