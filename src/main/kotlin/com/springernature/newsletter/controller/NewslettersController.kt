package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.service.INewsletterService
import org.hibernate.validator.constraints.Email
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class NewslettersController(val newsletterService: INewsletterService) {

    @RequestMapping(path = arrayOf("/newsletters"))
    fun find(@Valid @Email @RequestParam (value = "email", required = true) email: String) : Newsletter  {
        println("email: " + email)
        return newsletterService.findNewslettersByEmail(email)
    }

}