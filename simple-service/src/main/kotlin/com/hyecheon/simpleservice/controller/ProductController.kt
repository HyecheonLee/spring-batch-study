package com.hyecheon.simpleservice.controller

import com.hyecheon.simpleservice.model.*
import org.springframework.web.bind.annotation.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@RestController
class ProductController {
	@GetMapping(value = ["/products"])
	fun getProducts() = run {
		arrayListOf<Product>(
			Product(1, "Apple", "Apple Cell from service", (300.00).toBigDecimal(), 10),
			Product(2, "Dell", "Dell computer from service", (700.00).toBigDecimal(), 10)
		)
	}
}