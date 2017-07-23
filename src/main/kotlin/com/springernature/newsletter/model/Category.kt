package com.springernature.newsletter.model

import org.springframework.data.annotation.Id

data class Category @JvmOverloads constructor(@Id val code: String = "", val title: String = "", val superCategoryCode: String? = null)
