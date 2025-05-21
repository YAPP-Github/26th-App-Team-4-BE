package com.yapp.yapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YappApplication

fun main(args: Array<String>) {
    runApplication<YappApplication>(*args)
}
