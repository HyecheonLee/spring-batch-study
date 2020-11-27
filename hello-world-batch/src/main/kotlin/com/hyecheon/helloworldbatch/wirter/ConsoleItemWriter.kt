package com.hyecheon.helloworldbatch.wirter

import org.springframework.batch.item.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
class ConsoleItemWriter : ItemWriter<Any> {

	override fun write(items: MutableList<out Any>) {
		items.forEach { println(it) }
		println("*** writing each chunk ***")
	}
}