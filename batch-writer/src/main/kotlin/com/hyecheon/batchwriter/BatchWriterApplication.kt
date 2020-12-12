package com.hyecheon.batchwriter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchWriterApplication

fun main(args: Array<String>) {
	runApplication<BatchWriterApplication>(*args)
}
