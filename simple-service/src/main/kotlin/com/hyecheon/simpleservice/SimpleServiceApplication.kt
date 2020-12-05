package com.hyecheon.simpleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleServiceApplication

fun main(args: Array<String>) {
	runApplication<SimpleServiceApplication>(*args)
}
