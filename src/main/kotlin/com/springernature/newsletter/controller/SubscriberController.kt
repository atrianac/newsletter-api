package com.springernature.newsletter.controller

import com.springernature.newsletter.model.Subscriber
import com.springernature.newsletter.repository.SubscriberRepository
import com.springernature.newsletter.service.ICategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.ValidatorFactory

@RestController
class SubscriberController(val subscriberRepository: SubscriberRepository, val categoryService: ICategoryService) {

    @RequestMapping(path = arrayOf("/subscribers"), method = arrayOf(RequestMethod.GET))
    fun list() = subscriberRepository.findAll()

    @RequestMapping(path = arrayOf("/subscribers"), method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody @Valid subscriber: Subscriber) : ResponseEntity<Subscriber> =
            if(categoryService.categoriesAreValid(subscriber.categoryCodes))
                if (subscriberRepository.exists(subscriber.email)) ResponseEntity(subscriberRepository.save(subscriber), HttpStatus.OK)
                else ResponseEntity(subscriberRepository.save(subscriber), HttpStatus.CREATED)
            else ResponseEntity(HttpStatus.BAD_REQUEST)

}
