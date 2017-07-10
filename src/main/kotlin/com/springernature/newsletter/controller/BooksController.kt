package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.repository.BookRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BooksController(val bookRepository: BookRepository) {

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.GET))
    fun list() = bookRepository.findAll()

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody book: Book) : Book = bookRepository.save(book)
}
