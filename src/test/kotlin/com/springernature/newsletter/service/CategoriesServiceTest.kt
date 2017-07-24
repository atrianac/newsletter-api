package com.springernature.newsletter.service

import com.springernature.newsletter.model.Category
import com.springernature.newsletter.repository.CategoryRepository
import com.springernature.newsletter.repository.ICategoryCustomRepository
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
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
class CategoriesServiceTest {

    @Autowired
    lateinit var categoriesService: ICategoriesService

    @MockBean
    lateinit var categoryRepository: CategoryRepository

    @MockBean
    lateinit var customCategoryRepository: ICategoryCustomRepository

    @Configuration
    @ComponentScan(basePackageClasses = arrayOf(CategoriesService::class), useDefaultFilters = false, includeFilters = arrayOf(ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CategoriesService::class)))
    open class ContextConfiguration

    @Test
    fun categoriesAreValidTest() {
        val categoriesCodes = listOfCategories.map { it.code }
        given(customCategoryRepository.findByListOfCategories(categoriesCodes)).willReturn(listOfCategories)
        assertThat(categoriesService.categoriesAreValid(categoriesCodes), Is.`is`(true))
    }

    @Test
    fun categoriesAreInvalidValidTest() {
        val categoriesCodes = listOfCategories.map { it.code }
        given(customCategoryRepository.findByListOfCategories(categoriesCodes)).willReturn(listOfCategories.drop(1))
        assertThat(categoriesService.categoriesAreValid(categoriesCodes), Is.`is`(false))
    }

    @Test
    fun calculateCategoryPathTest() {
        given(categoryRepository.findAll()).willReturn(listOfCategories)
        val categoryPath = categoriesService.calculateCategoryPath("fp")
        assertThat(categoryPath, Is.`is`(listOf("sc", "en", "sf", "fp")))
    }

    val listOfCategories by lazy {
        listOf(
                Category("sc", "Science"),
                Category("en", "Engineering", "sc"),
                Category("sf", "Software", "en"),
                Category("fp", "Functional programming", "sf"),
                Category("ob", "Object Oriented programming", "sf")
        )
    }

}
