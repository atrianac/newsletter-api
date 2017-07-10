package com.springernature.newsletter.model

import org.springframework.data.annotation.Id

data class Category(@Id val code: String, val title: String, val superCategoryCode: String?)
