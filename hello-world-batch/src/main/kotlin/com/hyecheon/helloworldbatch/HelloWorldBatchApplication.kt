package com.hyecheon.helloworldbatch

import org.springframework.batch.core.configuration.annotation.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
open class HelloWorldBatchApplication

fun main(args: Array<String>) {
	runApplication<HelloWorldBatchApplication>(*args)
}