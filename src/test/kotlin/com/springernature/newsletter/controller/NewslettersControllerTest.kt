package com.springernature.newsletter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.model.Subscriber
import com.springernature.newsletter.service.INewslettersService
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
@WebMvcTest(NewslettersController::class)
class NewslettersControllerTest {

    @MockBean
    lateinit var subscriberRepository: INewslettersService

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
        given(subscriberRepository.findNewslettersByEmail(Matchers.anyString())).willReturn(newsletter)

        mvc.perform(get("/newsletters").param("email", newsletter.recipient)).andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.recipient", Is.`is`(newsletter.recipient)))
                .andExpect(jsonPath("$.notifications.[0].title", Is.`is`(listOfNotification[0].title)))
                .andExpect(jsonPath("$.notifications.[1].title", Is.`is`(listOfNotification[1].title)))
    }

    val categories = listOf("en", "pr", "fp")

    val listOfNotification = listOf(
            Notification("Functional Programming in Scala", listOf(listOf("en", "pr", "fp")), categories),
            Notification("Functional Programming in Java", listOf(listOf("en", "pr", "fp")), categories)
    )

    val newsletter = Newsletter("email@domain.com", listOfNotification)


}
