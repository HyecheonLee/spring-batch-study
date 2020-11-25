package com.hyecheon.helloworldbatch.reader

import org.springframework.batch.item.support.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
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