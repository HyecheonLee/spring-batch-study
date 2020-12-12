package com.hyecheon.batchwriter.config

import com.hyecheon.batchwriter.model.*
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.launch.support.*
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.*
import org.springframework.batch.item.file.mapping.*
import org.springframework.batch.item.file.transform.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.core.io.*
import java.time.*
import java.time.format.*

/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@EnableBatchProcessing
@Configuration
class BatchConfig(
	val steps: StepBuilderFactory,
	val jobs: JobBuilderFactory
) {

	@Bean
	fun reader() = run {
		val apply = FlatFileItemReader<Product>().apply {
			setResource(FileSystemResource("D:\\study-project\\spring-batch-study\\batch-writer\\input\\product.csv"))
			setLinesToSkip(1)
			setLineMapper(DefaultLineMapper<Product>().apply {
				setFieldSetMapper(
					BeanWrapperFieldSetMapper<Product>().apply {
						setTargetType(Product::class.java)
					}
				)
				setLineTokenizer(DelimitedLineTokenizer().apply {
					setNames("productId", "productName", "productDesc", "price", "unit")
					setDelimiter(",")
				})
			})
		}
		apply
	}

	@StepScope
	@Bean
	fun flatFileItemReader(
		@Value("#{jobParameters['inputFile']}")
		inputFile: FileSystemResource? = null
	): FlatFileItemReader<Product> {
		val reader = FlatFileItemReader<Product>()
		// step 1 let reader know where is the file
		reader.setResource(inputFile!!)
		reader.setLineMapper { s, _ ->
			val split = s.split(",")
			Product(split[0].toInt(), split[1], split[2], split[3].toBigDecimal(), split[4].toInt())
		}

		reader.setLinesToSkip(1)
		return reader
	}

	@Bean
	fun flatFileItemWriter() = run {
		FlatFileItemWriter<Product>().apply {
			val fileSystemResource = FileSystemResource("D:\\study-project\\spring-batch-study\\batch-writer\\output\\product.csv")
//			println(fileSystemResource.)
			setResource(fileSystemResource)
			setLineAggregator(DelimitedLineAggregator<Product>().apply {
				setDelimiter("|")
				setFieldExtractor(BeanWrapperFieldExtractor<Product>().apply {
					setNames(arrayOf("productId", "productName", "productDesc", "price", "unit"))
				})
			})
			setHeaderCallback { writer ->
				writer.write("productId,productName,productDesc,price,unit")
			}
			setAppendAllowed(true)
			setFooterCallback { writer ->
				writer.write("this is created at ${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}")
			}
		}
	}

	@Bean
	fun step1() = run {
		steps.get("step1")
			.chunk<Product, Product>(1)
			.reader(reader())
			.writer(flatFileItemWriter())
			.build()
	}


	@Bean
	fun job1() = run {
		jobs.get("job1")
			.start(step1())
			.build()
	}
}