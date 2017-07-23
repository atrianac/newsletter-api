package com.springernature.newsletter.repository

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.springernature.newsletter.model.Subscriber
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
class SubscriberCustomRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var subscriberCustomRepository: ISubscriberCustomRepository

    @Autowired
    lateinit var subscriberRepository: SubscriberRepository

    @Configuration
    @ComponentScan(basePackages = arrayOf("com.springernature.newsletter.repository"))
    open class ContextConfiguration {
        @Bean
        open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
    }

    @Before
    fun init() {
        listOfBooks.forEach { subscriber -> subscriberRepository.save(subscriber) }
    }

    @Test
    fun findByListOfCategoriesTest() {
        assertThat(subscriberCustomRepository.findByEmail(listOfBooks[0].email).isDefined, Is.`is`(true))
    }

    val listOfBooks by lazy {
        listOf(
                Subscriber("subscriber.0@email.com", listOf("mt", "pr")),
                Subscriber("subscriber.1@email.com", listOf("mt", "cl")),
                Subscriber("subscriber.1@email.com", listOf("mt"))
        )
    }
}