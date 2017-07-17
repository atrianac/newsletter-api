package com.springernature.newsletter.model

import com.fasterxml.jackson.annotation.JsonView
import com.springernature.newsletter.model.views.Api
import org.springframework.data.annotation.Id

data class Category(@Id val code: String, val title: String, val superCategoryCode: String?)
