package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Category
import com.springernature.newsletter.repository.CategoryRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
class CategoriesController(val categoryRepository: CategoryRepository) {

    @RequestMapping(path = arrayOf("/categories"), method = arrayOf(RequestMethod.GET))
    fun list() = categoryRepository.findAll()

    @RequestMapping(path = arrayOf("/categories"), method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody category: Category) : Category = categoryRepository.save(category)
}
