package com.springernature.newsletter.model

import org.springframework.data.annotation.Id

data class Book(@Id val title: String, val categoryCodes: List<String>)