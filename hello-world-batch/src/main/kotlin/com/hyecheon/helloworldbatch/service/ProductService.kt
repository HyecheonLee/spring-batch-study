package com.hyecheon.helloworldbatch.service

import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.*
import com.hyecheon.helloworldbatch.model.*
import org.springframework.stereotype.*
import java.net.*
import java.net.http.*
import java.net.http.HttpResponse.*


/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Service
class ProductService(
	private val objectMapper: ObjectMapper
) {
	fun getProducts() = run {
		val client = HttpClient.newHttpClient()
		val request = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:8080/products"))
			.build()

		val response = client.send(request, BodyHandlers.ofString())
		val typeReference: TypeReference<List<Product>> = object : TypeReference<List<Product>>() {}
		objectMapper.readValue(response.body(), typeReference)
	}
}