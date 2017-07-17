package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.repository.BookRepository
 import com.springernature.newsletter.service.ICategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BooksController(val bookRepository: BookRepository, val categoryService: ICategoryService) {

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.GET))
    fun list() = bookRepository.findAll()

    @RequestMapping(path = arrayOf("/books"), method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody book: Book) : ResponseEntity<Book> =
         if(categoryService.categoriesAreValid(book.categoryCodes))
            if (bookRepository.exists(book.title)) ResponseEntity(bookRepository.save(book), HttpStatus.OK)
            else ResponseEntity(bookRepository.save(book), HttpStatus.CREATED)
         else ResponseEntity(HttpStatus.BAD_REQUEST)

}
