package com.deepromeet.atcha.support

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.yapp.user.domain.UserRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseControllerTest {
    @Autowired
    lateinit var userRepository: UserRepository

    protected val objectMapper = jacksonObjectMapper()

    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setPort() {
        RestAssured.port = port
    }
}
