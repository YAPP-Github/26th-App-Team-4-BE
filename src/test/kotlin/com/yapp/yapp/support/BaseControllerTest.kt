package com.deepromeet.atcha.support

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yapp.yapp.user.domain.UserRepository
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseControllerTest {
    companion object {
        @Container
        @JvmStatic
        val redisContainer =
            GenericContainer("redis:latest").apply {
                withExposedPorts(6379)
                waitingFor(Wait.forListeningPort())
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun redisProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host") { redisContainer.host }
            registry.add("spring.data.redis.port") { redisContainer.getMappedPort(6379) }
        }
    }

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
