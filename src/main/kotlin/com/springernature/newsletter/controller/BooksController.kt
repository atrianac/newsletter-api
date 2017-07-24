package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.repository.BookRepository
import com.springernature.newsletter.service.IBooksService
import com.springernature.newsletter.service.ICategoriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BooksController(val bookRepository: BookRepository, val categoryService: ICategoriesService, val bookService: IBooksService) {

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.GET))
    fun list() = bookRepository.findAll()

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody book: Book) : ResponseEntity<Book> =
         if(categoryService.categoriesAreValid(book.categoryCodes))
            if (bookRepository.exists(book.title)) ResponseEntity(bookService.createBook(book).first, HttpStatus.OK)
            else ResponseEntity(bookService.createBook(book).first, HttpStatus.CREATED)
         else ResponseEntity(HttpStatus.BAD_REQUEST)

}
