# 스프링 배치01

[juneyr.dev 블로그 참고]:https://juneyr.dev/2019-07-24/spring-batch-tasklet
> 스프링 기본 설정에 @EnableBatchProcessing 어노테이션 추가 하기.  
> 스프링 배치시 필수로 사용해야 합니다.

```kotlin
@EnableBatchProcessing // 배치기능 활성화
@SpringBootApplication
class HelloWorldBatchApplication

fun main(args: Array<String>) {
	runApplication<HelloWorldBatchApplication>(*args)
}
```

## 스프링 배치

> 배치작업 하나의 작업 단위는 job, job 안에는 여러 단계의 step이 존재하고 각 step은 Tasklet으로 구성
> 즉 배치 작업 하나가 job에 해당

```kotlin
@Configuration
class HelloBatch(
	val jobs: JobBuilderFactory,
	val steps: StepBuilderFactory
) {

	@Bean // 하나의 step
	fun step1() = run {
		steps.get("step1")
			.tasklet(helloWorldTasklet())
			.build()
	}

	// Tasklet은 step 에서 하고 싶은 내용
	private fun helloWorldTasklet(): Tasklet {
		return (Tasklet { contribution, chunkContext ->
			println("hello world")
			RepeatStatus.FINISHED
		})
	}

	@Bean // 하나의 job
	public fun helloWorldJob() = run {
		jobs.get("helloWorldJob")
			.start(step1())
			.build()
	}
}
```

> helloWorldJob 안에는 step1 이 존재 하고 step1에는 helloWorldTasklet 이 존재 한다.