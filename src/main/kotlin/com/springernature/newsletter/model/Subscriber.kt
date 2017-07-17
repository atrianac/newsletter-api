package com.springernature.newsletter.model


import org.hibernate.validator.constraints.Email
import org.springframework.data.annotation.Id

data class Subscriber(@get:Id @get:Email val email: String, val categoryCodes: List<String>)
