package com.springernature.newsletter.repository

import com.springernature.newsletter.model.Notification
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository

import org.springframework.stereotype.Repository

interface NotificationRepository : MongoRepository<Notification, String>

interface INotificationCustomRepository {
    fun findByCategories(categories: List<String>) : List<Notification>
}

@Repository
open class NotificationCustomRepository(val mongoTemplate: MongoTemplate) : INotificationCustomRepository {
    override fun findByCategories(categories: List<String>) : List<Notification>  {
        val query = Query().addCriteria(Criteria.where("categories").`in`(categories))
        return mongoTemplate.find(query, Notification::class.java)
    }
}