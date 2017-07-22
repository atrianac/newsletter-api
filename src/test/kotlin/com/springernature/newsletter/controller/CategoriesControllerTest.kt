package com.springernature.newsletter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.springernature.newsletter.model.Category
import com.springernature.newsletter.repository.CategoryRepository
import com.springernature.newsletter.service.ICategoriesService
import org.junit.*
import org.junit.runner.*
import org.springframework.boot.test.autoconfigure.web.servlet.*
import org.springframework.boot.test.mock.mockito.*
import org.hamcrest.core.Is
import org.mockito.BDDMockito.*;
import org.mockito.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner::class)
@WebMvcTest(CategoriesController::class)
class CategoriesControllerTest {

    @MockBean
    lateinit var categoryRepository: CategoryRepository

    @MockBean
    lateinit var categoryService: ICategoriesService

    @Autowired
    lateinit var mvc: MockMvc

    lateinit var categoryJson: JacksonTester<Category>

    lateinit var objectMapper: ObjectMapper

    @Before
    fun setup() {
        objectMapper = ObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }


    @Test
    fun testFind() {
        given(categoryRepository.findAll()).willReturn(listOfCategories)

        mvc.perform(get("/categories")).andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].title", Is.`is`(listOfCategories[0].title)))
                .andExpect(jsonPath("$.[1].title", Is.`is`(listOfCategories[1].title)))
                .andExpect(jsonPath("$.[2].title", Is.`is`(listOfCategories[2].title)))
    }

    @Test
    fun testCreate() {
        given(categoryService.categoriesAreValid(listOf(category.superCategoryCode) as List<String>)).willReturn(true)
        given(categoryRepository.exists(Matchers.anyString())).willReturn(false)
        given(categoryRepository.save(category)).willReturn(category)

        mvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON_UTF8).content(categoryJson.write(category).json))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCreateNotValidCategories() {
        given(categoryService.categoriesAreValid(listOf())).willReturn(false)
        mvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON_UTF8).content(categoryJson.write(category).json))
                .andExpect(status().isBadRequest)
    }

    val listOfCategories by lazy {
        listOf(
                Category("sc", "Science"),
                Category("en", "Engineering", "sc"),
                Category("sf", "Software", "en")
        )
    }

    val category =  Category("en", "Engineering", "sc")
}
