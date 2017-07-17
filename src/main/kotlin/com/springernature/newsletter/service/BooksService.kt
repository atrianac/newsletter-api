package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.repository.BookRepository
import org.springframework.stereotype.Service

interface IBooksService {
    fun createBook(book: Book): Book
}

@Service
class BooksService(val bookRepository: BookRepository, val newslettersService: INewslettersService) : IBooksService {
    override fun createBook(book: Book): Book {
        val bookCreated = bookRepository.save(book)
        newslettersService.createNotificationForBook(book)
        return bookCreated
    }
}
