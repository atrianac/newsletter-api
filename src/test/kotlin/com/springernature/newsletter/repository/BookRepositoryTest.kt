package com.springernature.newsletter.repository

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.springernature.newsletter.model.Book
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataMongoTest
class BookCustomRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var bookCustomRepository: IBookCustomRepository

    @Autowired
    lateinit var bookRepository: BookRepository

    @Configuration
    @ComponentScan(basePackages = arrayOf("com.springernature.newsletter.repository"))
    open class ContextConfiguration {
        @Bean
        open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
    }

    @Before
    fun init() {
        listOfBooks.forEach { book -> bookRepository.save(book) }
    }

    @Test
    fun findByListOfCategoriesTest() {
        assertThat(bookCustomRepository.findBooksByCategories(listOf("pr")).size, Is.`is`(2))
    }

    val listOfBooks by lazy {
        listOf(
                Book("Functional Programming in Scala", listOf("mt", "pr")),
                Book("Domain Driven Design", listOf("pr")),
                Book("Calculus", listOf("mt"))
        )
    }
}
