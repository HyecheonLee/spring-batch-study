package com.hyecheon.helloworldbatch.listener

import org.springframework.batch.core.*
import org.springframework.stereotype.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */

@Component
class HelloWorldJobExecutionListener : JobExecutionListener {

	override fun beforeJob(jobExecution: JobExecution) {
		println("before starting the Job - Job name : ${jobExecution.jobInstance.jobName}")
		println("before starting the Job ${jobExecution.executionContext}")

		jobExecution.executionContext.put("my name", "hyecheon")
		println("before starting the Job - after set ${jobExecution.executionContext}")
	}

	override fun afterJob(jobExecution: JobExecution) {
		println("after starting the Job - Job Execution Context ${jobExecution.executionContext}")
	}
}