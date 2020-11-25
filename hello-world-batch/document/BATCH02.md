# 스프링 배치02

> spring batch 는 각 1개의 job 단위로 여러 개의 step 을 포함하여 실행한다.
> StepExecutionListener 와 JobExecutionListener 을 통해 확인해 보자

```kotlin
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
```

> JobExecutionListener 는 2개의 beforeJob,afterJob 을 override 한다.  
> 2개의 메소드는 각 job의 실행 전 후에 실행 된다.  
> StepExecutionListener 는 2개의 beforeStep,afterStep 을 override 한다.  
> 2개의 메소드는 각 step 실행 전 후에 실행 된다.

```kotlin
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
			.listener(stepExecutionListener) // <- 위에서 만든 stepExecutionListener 를 추가 한다.
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
			.listener(jobExecutionListener)  // <- 위에서 만든 jobExecutionListener 를 추가 한다.
			.start(step1())
			.build()
	}
}
```

```
before starting the Job - Job name : helloWorldJob
before starting the Job {}
before starting the Job - after set {my name=hyecheon}
2020-11-26 00:51:56.900  INFO 16080 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
this is from Before Step Execution : {my name=hyecheon}
In side Step - print job parameters  {jobRunDate=2020-11-25}
hello world
this is from After Step Execution : {my name=hyecheon}
2020-11-26 00:51:56.912  INFO 16080 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 11ms
after starting the Job - Job Execution Context {my name=hyecheon}
```
> hello world job 안에 step 이 실행 되고 step의 Tasklet이 실행된다.
> 실행된 job의 executionContext 의 값이 step 에서도 동일한 값으로 설정된 것을 확인 할 수 있다.  
> 즉 하나의 실행 단위로 Context를 사용 가능하다.