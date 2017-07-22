package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Category
import com.springernature.newsletter.repository.CategoryRepository
import com.springernature.newsletter.service.ICategoriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CategoriesController(val categoryRepository: CategoryRepository, val categoryService: ICategoriesService) {

    @RequestMapping(path = arrayOf("/categories"), method = arrayOf(RequestMethod.GET))
    fun list() = categoryRepository.findAll()

    @RequestMapping(path = arrayOf("/categories"), method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody category: Category) : ResponseEntity<Category> =
        if(validateCategory(category))
            if(categoryRepository.exists(category.code)) ResponseEntity(categoryRepository.save(category), HttpStatus.OK)
            else ResponseEntity(categoryRepository.save(category), HttpStatus.CREATED)
        else ResponseEntity(HttpStatus.BAD_REQUEST)

    fun validateCategory(category: Category) =
            if(category.superCategoryCode != null)
                categoryService.categoriesAreValid(listOf(category.superCategoryCode))
            else true

}
