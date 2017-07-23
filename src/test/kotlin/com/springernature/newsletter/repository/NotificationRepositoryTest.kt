package com.springernature.newsletter.repository

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.Notification
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
class NotificationCustomRepositoryTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var notificationCustomRepository: INotificationCustomRepository

    @Autowired
    lateinit var notificationRepository: NotificationRepository

    @Configuration
    @ComponentScan(basePackages = arrayOf("com.springernature.newsletter.repository"))
    open class ContextConfiguration {
        @Bean
        open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
    }

    @Before
    fun init() {
        listOfNotification.forEach { notificationRepository.save(it)  }
    }

    @Test
    fun findByListOfCategoriesTest() {
        assertThat(notificationCustomRepository.findByCategories(categories).isEmpty(), Is.`is`(false))
    }

    val categories = listOf("en", "pr", "fp")

    val listOfNotification = listOf(
            Notification("Functional Programming in Scala", listOf(listOf("en", "pr", "fp")), categories),
            Notification("Functional Programming in Java", listOf(listOf("en", "pr", "fp")), categories)
    )

    val newsletter = Newsletter("email@domain.com", listOfNotification)
}
