package com.hyecheon.helloworldbatch

import org.springframework.batch.core.configuration.annotation.*
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@EnableBatchProcessing
@SpringBootApplication
class HelloWorldBatchApplication

fun main(args: Array<String>) {
	runApplication<HelloWorldBatchApplication>(*args)
}