package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.repository.BookRepository
import kotlinx.coroutines.experimental.Deferred
import org.springframework.stereotype.Service

interface IBooksService {
    fun createBook(book: Book): Pair<Book, Deferred<Notification>>
}

@Service
class BooksService(val bookRepository: BookRepository, val newslettersService: INewslettersService) : IBooksService {
    override fun createBook(book: Book): Pair<Book, Deferred<Notification>> {
        val bookCreated = bookRepository.save(book)
        val notification = newslettersService.createNotificationForBook(book)
        return Pair(bookCreated, notification)
    }
}
