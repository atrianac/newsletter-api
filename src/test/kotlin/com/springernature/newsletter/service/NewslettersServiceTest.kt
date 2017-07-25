package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.model.Category
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.model.Subscriber
import com.springernature.newsletter.repository.*
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Matchers.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class NewslettersServiceTest {

    @MockBean
    lateinit var notificationRepository: NotificationRepository

    @MockBean
    lateinit var categoryService: ICategoriesService

    @MockBean
    lateinit var subscriberRepository: SubscriberRepository

    @MockBean
    lateinit var notificationCustomRepository: INotificationCustomRepository

    @Autowired
    lateinit var newslettersService: INewslettersService

    @Configuration
    @ComponentScan(basePackageClasses = arrayOf(NewslettersService::class), useDefaultFilters = false, includeFilters = arrayOf(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewslettersService::class)))
    open class ContextConfiguration

    @Test
    fun createNotificationForBookTest() {
        given(categoryService.calculateCategoryPath("fp")).willReturn(categoryPathFp)
        given(categoryService.calculateCategoryPath("op")).willReturn(categoryPathOp)

        val categoryPath = listOf(categoryPathFp, categoryPathOp)
        given(notificationRepository.save(anyObject<Notification>())).willReturn(Notification(book.title, categoryPath,categoryPathFp.plus(categoryPathOp)))

        runBlocking {
            val notification = newslettersService.createNotificationForBook(book).await()
            assertThat(notification.title, Is.`is`(book.title))
            assertThat(notification.categoryPath, Is.`is`(categoryPath))
        }
    }

    @Test
    fun findNewslettersByEmailTest() {
        given(subscriberRepository.findOne(subscriber.email)).willReturn(subscriber)
        given(notificationCustomRepository.findByCategories(subscriber.categoryCodes)).willReturn(listOfNotification)

        val newsletter = newslettersService.findNewslettersByEmail(subscriber.email)
        assertThat(newsletter.recipient, Is.`is`(subscriber.email))
        assertThat(newsletter.notifications.isEmpty(), Is.`is`(false))
    }


    val subscriber =  Subscriber("email@domain.com", listOf("pr"))

    val categoryPathFp = listOf("en", "pr", "fp")

    val categoryPathOp = listOf("en", "pr", "op")

    val book =  Book("Functional Programming in Scala", listOf("fp", "op"))

    val listOfNotification = listOf(
            Notification("Functional Programming in Scala", listOf(categoryPathFp), categoryPathFp),
            Notification("Functional Programming in Java", listOf(categoryPathOp), categoryPathOp)
    )
}
