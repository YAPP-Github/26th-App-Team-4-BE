package com.yapp.yapp.support

import com.yapp.yapp.support.fixture.UserFixture
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseServiceTest : BaseSupportMethod() {
    @Autowired
    lateinit var userFixture: UserFixture
}
