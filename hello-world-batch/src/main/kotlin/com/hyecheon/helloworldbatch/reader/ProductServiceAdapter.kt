package com.hyecheon.helloworldbatch.reader

import com.hyecheon.helloworldbatch.model.*
import com.hyecheon.helloworldbatch.service.*
import org.springframework.beans.factory.*
import org.springframework.stereotype.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Component
class ProductServiceAdapter(
	private val productService: ProductService
) : InitializingBean {
	val products: MutableList<Product> = mutableListOf()
	override fun afterPropertiesSet() {
		products.addAll(productService.getProducts().toMutableList())
	}

	fun nextProduct(): Product? {
		return if (products.isNotEmpty()) {
			products.removeAt(0)
		} else {
			null
		}
	}
}