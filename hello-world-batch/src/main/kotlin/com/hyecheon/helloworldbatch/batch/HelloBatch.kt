package com.hyecheon.helloworldbatch.batch

import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.step.tasklet.*
import org.springframework.batch.repeat.*
import org.springframework.context.annotation.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Configuration
class HelloBatch(
	val jobs: JobBuilderFactory,
	val steps: StepBuilderFactory
) {

	@Bean
	fun step1() = run {
		steps.get("step1")
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
			.start(step1())
			.build()
	}
}