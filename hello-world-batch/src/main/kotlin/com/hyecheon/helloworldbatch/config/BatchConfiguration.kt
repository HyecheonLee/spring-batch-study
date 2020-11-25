package com.hyecheon.helloworldbatch.config

import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.step.tasklet.*
import org.springframework.batch.repeat.*
import org.springframework.context.annotation.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Configuration
@EnableBatchProcessing
class BatchConfiguration(
	val jobs: JobBuilderFactory,
	val steps: StepBuilderFactory,
	val jobExecutionListener: JobExecutionListener,
	val stepExecutionListener: StepExecutionListener
) {

	@Bean
	fun step1() = run {
		steps.get("step1")
			.listener(stepExecutionListener)
			.tasklet(helloWorldTasklet())
			.build()
	}

	private fun helloWorldTasklet(): Tasklet {
		return (Tasklet { contribution, chunkContext ->
			println("hello world")
			RepeatStatus.FINISHED
		})
	}

	@Bean
	public fun helloWorldJob() = run {
		jobs.get("helloWorldJob")
			.listener(jobExecutionListener)
			.start(step1())
			.build()
	}
}