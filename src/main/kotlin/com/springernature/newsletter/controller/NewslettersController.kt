package com.springernature.newsletter.controller

import com.fasterxml.jackson.annotation.JsonView
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.views.Api
import com.springernature.newsletter.service.INewslettersService
import org.hibernate.validator.constraints.Email
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class NewslettersController(val newsletterService: INewslettersService) {

    @RequestMapping(path = arrayOf("/newsletters"))
    @JsonView(Api::class)
    fun find(@Valid @Email @RequestParam (value = "email", required = true) email: String) : Newsletter  {
        return newsletterService.findNewslettersByEmail(email)
    }

}