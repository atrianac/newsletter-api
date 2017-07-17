package com.springernature.newsletter.model

import com.fasterxml.jackson.annotation.JsonView
import com.springernature.newsletter.model.views.Api
import org.springframework.data.annotation.Id

data class Notification(@Id @JsonView(Api::class) val title: String, @JsonView(Api::class) val categoryPath: List<List<String>>, val categories: List<String>)

data class Newsletter(@Id @JsonView(Api::class) val recipient: String, @JsonView(Api::class) val notifications: List<Notification>)
