package com.hyecheon.helloworldbatch.wirter

import org.springframework.batch.item.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
class ConsoleItemWriter : ItemWriter<Int> {
	override fun write(items: MutableList<out Int>) {
		items.forEach { println(it) }
		println("*** writing each chunk ***")
	}
}