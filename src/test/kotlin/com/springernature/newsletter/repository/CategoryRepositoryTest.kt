package com.springernature.newsletter.repository

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.springernature.newsletter.model.Category
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
class CategoryCustomRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var categoryCustomRepository: ICategoryCustomRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Configuration
    @ComponentScan(basePackages = arrayOf("com.springernature.newsletter.repository"))
    open class ContextConfiguration {
        @Bean
        open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
    }

    @Before
    fun init() {
        listOfCategories.forEach { category -> categoryRepository.save(category) }
    }

    @Test
    fun findByListOfCategoriesTest() {
        assertThat(categoryCustomRepository.findByListOfCategories(listOf("sc", "en")).size, Is.`is`(2))
    }

    val listOfCategories by lazy {
        listOf(
                Category("sc", "Science"),
                Category("en", "Engineering", "sc"),
                Category("sf", "Software", "en")
        )
    }
}
