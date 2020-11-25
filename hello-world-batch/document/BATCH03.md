# 스프링 배치03

> 스프링 배치에서 step 에는 [Tasklet] , [Reader & Processor & Writer] 존재  
> step 을 3단계로 실행 하는 reader,processor,writer 만들어 보기

## Reader 만들기

```kotlin
class InMemReader : AbstractItemStreamItemReader<Int>() {
	private val myList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
	private var index = 0
	override fun read(): Int? {
		var nextItem: Int? = null
		if (index < myList.size) {
			nextItem = myList[index]
			index++
		} else {
			index = 0
		}
		return nextItem
	}
}
```

### 인메모리 list를 읽는 reader를 구현 하자.

* AbstractItemStreamItemReader 를 확장해서 만든다.
* override fun read() 를 구현 해서 읽은 값을 설정 한다.
* null 일 경우 read를 멈춘다.

---

## Processor 만들기

```kotlin
class InMemItemProcessor : ItemProcessor<Int, Int> {
	override fun process(item: Int): Int {
		return item + 10
	}
}
```

### reader에서 읽은 값을 변환 하는 processor 클래스를 구현하자

* ItemProcessor 를 확장해서 만든다.
* override fun process(item: Int) 안에 reader에서 읽은 값을 변환 하는 작업을 기술 한다.
* 여기선 인메모리에 리스트에서 읽은 값에 10을 더 하는 작업을 추가 한다.

---

## Writer 만들기

```kotlin
class ConsoleItemWriter : ItemWriter<Int> {
	override fun write(items: MutableList<out Int>) {
		items.forEach { println(it) }
		println("*** writing each chunk ***")
	}
}
```

### processor에서 변환한 값을 chunk단위로 실행 하는 writer 클래스 구현

* ItemWriter<Int> 를 확장해서 만든다.
* override fun write(items: MutableList<out Int>) 안에 processor에서 변환한 값을 실행한다.
* 여기서 List로 값을 받는 이유는 write는 chunk 단위로 묶어서 실행하기 때문이다.
* 결론적으로 write 작업에는 processor의 각각의 단업을 chunk 단위로 묶어서 큰 작업을 한번에 실행 한다.

---

## step 생성 하기

```
@Bean
fun step2() = run {
    steps.get("step2")
        .chunk<Int, Int>(3)
        .reader(reader())
        .processor(processor())
        .writer(ConsoleItemWriter())
        .build()
}
```

1. chunk() 를 실행 chunk 단위를 입력 한다. chunk 단위는 마지막 write의 실행 묶음 단위 이다.
2. reader를 만든다.
3. processor를 만든다.
4. writer를 만든다.

## 실행 결과
```
2020-11-26 01:31:43.101  INFO 16420 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
11
12
13
*** writing each chunk ***
14
15
16
*** writing each chunk ***
17
18
19
*** writing each chunk ***
20
*** writing each chunk ***
2020-11-26 01:31:43.112  INFO 16420 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step2] executed in 11ms
```

> chunk 단위 3이므로 3개의 작업 묶음이 console write로 실행 된다.