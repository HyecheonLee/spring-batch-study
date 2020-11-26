package com.hyecheon.helloworldbatch.config

import com.hyecheon.helloworldbatch.processor.*
import com.hyecheon.helloworldbatch.reader.*
import com.hyecheon.helloworldbatch.wirter.*
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.launch.support.*
import org.springframework.batch.core.step.tasklet.*
import org.springframework.batch.item.*
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

	@Bean
	fun step2() = run {
		steps.get("step2")
			.chunk<Int, Int>(3)
			.reader(reader())
			.processor(processor())
			.writer(ConsoleItemWriter())
			.build()
	}

	@Bean
	fun processor() = run {
		InMemItemProcessor()
	}

	@Bean
	fun reader(): ItemReader<out Int> {
		return InMemReader()
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
			.incrementer(RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(step1())
			.next(step2())
			.build()
	}
}