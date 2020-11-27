package com.hyecheon.helloworldbatch.model

import java.math.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
data class Product(
	var productID: Int? = null,
	var prodName: String? = null,
	var productDesc: String? = null,
	var price: BigDecimal? = null,
	var unit: Int? = null,
)