package com.hyecheon.batchwriter.model

import java.math.*
import javax.xml.bind.annotation.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@XmlRootElement(name = "product")
data class Product(
	var productId: Int? = null,
	@XmlElement(name = "productName")
	var productName: String? = null,
	var productDesc: String? = null,
	var price: BigDecimal? = null,
	var unit: Int? = null,
)