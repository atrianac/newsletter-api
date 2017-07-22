package com.springernature.newsletter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.springernature.newsletter.model.Book
import com.springernature.newsletter.repository.BookRepository
import com.springernature.newsletter.service.IBooksService
import com.springernature.newsletter.service.ICategoriesService
import org.junit.*
import org.junit.runner.*
import org.springframework.boot.test.autoconfigure.web.servlet.*
import org.springframework.boot.test.mock.mockito.*
import org.hamcrest.core.Is
import  org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner::class)
@WebMvcTest(BooksController::class)
class BooksControllerTest {

    @MockBean
    lateinit var bookRepository: BookRepository

    @MockBean
    lateinit var categoryService: ICategoriesService

    @MockBean
    lateinit var bookService: IBooksService

    @Autowired
    lateinit var mvc: MockMvc

    lateinit var bookJson: JacksonTester<Book>;

    lateinit var objectMapper: ObjectMapper

    @Before
    fun setup() {
        objectMapper = ObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }


    @Test
    fun testFind() {
        given(bookRepository.findAll()).willReturn(listOfBooks)

        mvc.perform(get("/books")).andExpect(status().isOk)
                                  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                  .andExpect(jsonPath("$.[0].title", Is.`is`(listOfBooks[0].title)))
                                  .andExpect(jsonPath("$.[1].title", Is.`is`(listOfBooks[1].title)))

    }

    @Test
    fun testCreate() {
        given(categoryService.categoriesAreValid(listOf())).willReturn(true)
        given(bookService.createBook(book)).willReturn(book)


        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON_UTF8).content(bookJson.write(book).json))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCreateNotValidCategories() {
        given(categoryService.categoriesAreValid(listOf())).willReturn(false)
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON_UTF8).content(bookJson.write(book).json))
                .andExpect(status().isBadRequest)
    }

    val listOfBooks by lazy {
        listOf(
                Book("Functional Programming in Scala", listOf()),
                Book("Functional Programming in JavaScript", listOf())
        )
    }

    val book = Book("Functional Programming in Scala", listOf())
}
