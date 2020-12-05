package com.hyecheon.simpleservice.model

import java.math.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
data class Product(
	var productId: Int? = null,
	var productName: String? = null,
	var productDesc: String? = null,
	var price: BigDecimal? = null,
	var unit: Int? = null,
)