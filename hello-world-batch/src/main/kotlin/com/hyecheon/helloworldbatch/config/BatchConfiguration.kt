package com.hyecheon.helloworldbatch.config

import com.hyecheon.helloworldbatch.model.*
import com.hyecheon.helloworldbatch.processor.*
import com.hyecheon.helloworldbatch.reader.*
import com.hyecheon.helloworldbatch.wirter.*
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.*
import org.springframework.batch.core.launch.support.*
import org.springframework.batch.core.step.tasklet.*
import org.springframework.batch.item.*
import org.springframework.batch.item.database.*
import org.springframework.batch.item.file.*
import org.springframework.batch.item.file.mapping.*
import org.springframework.batch.item.file.transform.*
import org.springframework.batch.item.xml.*
import org.springframework.batch.repeat.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.core.io.*
import org.springframework.jdbc.core.*
import org.springframework.oxm.jaxb.*
import javax.sql.*


/**
 * @author hyecheon
 * @email rainbow880616@gmail.com
 */
@Configuration
@EnableBatchProcessing
class BatchConfiguration(
	val jobs: JobBuilderFactory,
	val steps: StepBuilderFactory,
	val jobExecutionListener: JobExecutionListener,
	val stepExecutionListener: StepExecutionListener,
	val mariaDataSource: DataSource
) {

	@Bean
	fun step1() = run {
		steps.get("step1")
			.listener(stepExecutionListener)
			.tasklet(helloWorldTasklet())
			.build()
	}

	/*inputFile=hello-world-batch/input/product.csv*/
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

	@StepScope
	@Bean
	fun flatFileItemReade2(
		@Value("#{jobParameters['inputFile']}") inputFile: FileSystemResource? = null
	): FlatFileItemReader<Product> {
		val reader: FlatFileItemReader<Product> = FlatFileItemReader<Product>()
		// step 1 let reader know where is the file
		reader.setResource(inputFile!!)

		//create the line Mapper
		reader.setLineMapper(
			object : DefaultLineMapper<Product>() {
				init {
					setLineTokenizer(object : DelimitedLineTokenizer() {
						init {
							setNames(*arrayOf("prodId", "productName", "prodDesc", "price", "unit"))
							setDelimiter(",")
						}
					})
					setFieldSetMapper(object : BeanWrapperFieldSetMapper<Product>() {
						init {
							setTargetType(Product::class.java)
						}
					})
				}
			}
		)
		//step 3 tell reader to skip the header
		reader.setLinesToSkip(1)
		return reader
	}

	@StepScope
	@Bean
	fun xmlItemReader(
		@Value("#{jobParameters['inputFile']}") inputFile: FileSystemResource? = null
	) = run {
		StaxEventItemReader<Product>().apply {
			setResource(inputFile!!)
			//need to let reader to know which tags describe the domain object
			setFragmentRootElementName("product")
			setUnmarshaller(Jaxb2Marshaller().apply { setClassesToBeBound(Product::class.java) })
		}

	}

	@Bean
	fun jdbcCursorItemReader() = run {
		JdbcCursorItemReader<Product>().apply {
			this.dataSource = mariaDataSource
			sql = "select product_id, product_name, prod_desc as product_desc, unit, price from products"
			setRowMapper(BeanPropertyRowMapper<Product>().apply {
				setMappedClass(Product::class.java)
			})
		}
	}


	@Bean
	fun step2() = run {
		steps.get("step2")
			.chunk<Product, Product>(3)
//			.reader(reader())
//			.reader(flatFileItemReade2())
//			.reader(xmlItemReader())
			.reader(jdbcCursorItemReader())
			.writer(ConsoleItemWriter())
			.build()
	}

	@Bean
	fun processor() = run {
		InMemItemProcessor()
	}

	@Bean
	fun reader(): ItemReader<out Int> {
		return InMemReader()
	}

	private fun helloWorldTasklet(): Tasklet {
		return (Tasklet { contribution, chunkContext ->
			println("hello world")
			RepeatStatus.FINISHED
		})
	}

	@Bean
	fun helloWorldJob() = run {
		jobs.get("helloWorldJob")
			.incrementer(RunIdIncrementer())
			.listener(jobExecutionListener)
			.start(step1())
			.next(step2())
			.build()
	}
}