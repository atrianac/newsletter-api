package com.springernature.newsletter.service

import com.springernature.newsletter.repository.ICategoryCustomRepository
import org.springframework.stereotype.Service


interface ICategoryService {
    fun categoriesAreValid(categories: List<String>): Boolean
}

@Service
class CategoryService(val customCategoryRepository: ICategoryCustomRepository) : ICategoryService {

    override fun categoriesAreValid(categories: List<String>) =
            customCategoryRepository.findByListOfCategories(categories).size == categories.size

}
