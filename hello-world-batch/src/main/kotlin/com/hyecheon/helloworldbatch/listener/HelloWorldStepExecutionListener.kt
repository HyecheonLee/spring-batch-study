package com.hyecheon.helloworldbatch.listener

import org.springframework.batch.core.*
import org.springframework.stereotype.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Component
class HelloWorldStepExecutionListener : StepExecutionListener {
	override fun beforeStep(stepExecution: StepExecution) {
		println("this is from Before Step Execution : ${stepExecution.jobExecution.executionContext}")
		println("In side Step - print job parameters  ${stepExecution.jobExecution.jobParameters}")
	}

	override fun afterStep(stepExecution: StepExecution): ExitStatus? {
		println("this is from After Step Execution : ${stepExecution.jobExecution.executionContext}")
		return null
	}
}