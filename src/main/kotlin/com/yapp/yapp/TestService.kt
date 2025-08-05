package com.yapp.yapp

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TestService(
    private val testManager: TestManager,
) {
    @Transactional
    fun test() {
        println("test")
        testManager.requiredNewTest()
    }
}

@Component
class TestManager {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun requiredNewTest() {
        println("new transaction")
    }
}
