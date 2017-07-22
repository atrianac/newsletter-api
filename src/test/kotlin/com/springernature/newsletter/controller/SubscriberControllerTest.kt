package com.springernature.newsletter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.springernature.newsletter.model.Subscriber
import com.springernature.newsletter.repository.SubscriberRepository
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
@WebMvcTest(SubscriberController::class)
class SubscriberControllerTest {

    @MockBean
    lateinit var subscriberRepository: SubscriberRepository

    @MockBean
    lateinit var categoryService: ICategoriesService

    @Autowired
    lateinit var mvc: MockMvc

    lateinit var categoryJson: JacksonTester<Subscriber>

    lateinit var objectMapper: ObjectMapper

    @Before
    fun setup() {
        objectMapper = ObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }


    @Test
    fun testFind() {
        given(subscriberRepository.findAll()).willReturn(listOfSubscriber)

        mvc.perform(get("/subscribers")).andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].email", Is.`is`(listOfSubscriber[0].email)))
                .andExpect(jsonPath("$.[1].email", Is.`is`(listOfSubscriber[1].email)))
    }

    @Test
    fun testCreate() {
        given(categoryService.categoriesAreValid(listOf(subscriber.categoryCodes[0]))).willReturn(true)
        given(subscriberRepository.exists(Matchers.anyString())).willReturn(false)
        given(subscriberRepository.save(subscriber)).willReturn(subscriber)

        mvc.perform(post("/subscribers").contentType(MediaType.APPLICATION_JSON_UTF8).content(categoryJson.write(subscriber).json))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun testCreateNotValidCategories() {
        given(categoryService.categoriesAreValid(listOf())).willReturn(false)
        mvc.perform(post("/subscribers").contentType(MediaType.APPLICATION_JSON_UTF8).content(categoryJson.write(subscriber).json))
                .andExpect(status().isBadRequest)
    }

    val listOfSubscriber by lazy {
        listOf(
                Subscriber("email@domain.com", listOf()),
                Subscriber("email-2@domain.com", listOf())
        )
    }

    val subscriber =  Subscriber("email@domain.com", listOf("sc"))
}

