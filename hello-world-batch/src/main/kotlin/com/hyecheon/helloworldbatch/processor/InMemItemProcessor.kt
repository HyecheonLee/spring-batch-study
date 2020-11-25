package com.hyecheon.helloworldbatch.processor

import org.springframework.batch.item.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
class InMemItemProcessor : ItemProcessor<Int, Int> {
	override fun process(item: Int): Int {
		return item + 10
	}
}