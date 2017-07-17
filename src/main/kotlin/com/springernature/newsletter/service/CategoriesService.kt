package com.springernature.newsletter.service

import com.springernature.newsletter.repository.CategoryRepository
import com.springernature.newsletter.repository.ICategoryCustomRepository
import javaslang.Tuple
import javaslang.collection.Map
import javaslang.control.Option
import org.springframework.stereotype.Service


interface ICategoriesService {

    fun categoriesAreValid(categories: List<String>): Boolean

    fun calculateCategoryPath(category: String): List<String>

}

@Service
class CategoriesService(val customCategoryRepository: ICategoryCustomRepository, val categoryRepository: CategoryRepository) : ICategoriesService {

    private fun listCategoriesToMap(): Map<String, String> = javaslang.collection.List.ofAll(categoryRepository.findAll()).toMap { Tuple.of(it.code, it.superCategoryCode) }

    override fun categoriesAreValid(categories: List<String>) =
            customCategoryRepository.findByListOfCategories(categories).size == categories.size

    override fun calculateCategoryPath(category: String): List<String> {
        val categoriesMap = listCategoriesToMap()

        fun iter(currentCategory: Option<String>, categoryPath: javaslang.collection.List<String>) : javaslang.collection.List<String> =
                if(!currentCategory.isDefined) categoryPath.tail()
                else {
                    val vcategory = currentCategory.get()
                    iter(categoriesMap[vcategory], categoryPath.prepend(vcategory))
                }

        return iter(Option.of(category), javaslang.collection.List.empty()).toJavaList()
    }

}
