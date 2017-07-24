package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.repository.BookRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class BooksServiceTest {

    @Autowired
    lateinit var booksService: IBooksService

    @MockBean
    lateinit var bookRepository: BookRepository

    @MockBean
    lateinit var newslettersServices: NewslettersService

    @Configuration
    @ComponentScan(basePackageClasses = arrayOf(BooksService::class), useDefaultFilters = false, includeFilters = arrayOf(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BooksService::class)))
    open class ContextConfiguration

    @Test
    fun createBookTest() {
        given(bookRepository.save(book)).willReturn(book)
        given(newslettersServices.createNotificationForBook(book)).willReturn(notification)

        val (bookCreated, notification) = booksService.createBook(book)

        assertEquals(book.title, bookCreated.title)
        assertEquals(book.title, notification.getCompleted().title)
    }

    val book =  Book("Functional Programming in Scala", listOf())

    val notification = async(CommonPool) { Notification(book.title, listOf(book.categoryCodes), book.categoryCodes) }

}
