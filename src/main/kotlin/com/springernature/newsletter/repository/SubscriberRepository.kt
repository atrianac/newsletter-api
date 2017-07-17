package com.springernature.newsletter.repository;

import com.springernature.newsletter.model.Subscriber;
import javaslang.control.Option
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository


interface SubscriberRepository : MongoRepository<Subscriber, String>

interface  ISubscriberCustomRepository {
    fun findByEmail(email: String): Option<Subscriber>
}

@Repository
open class SubscriberCustomRepository(val mongoTemplate: MongoTemplate) : ISubscriberCustomRepository {

    override fun findByEmail(email: String): Option<Subscriber> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(email))
        val result = mongoTemplate.find(query, Subscriber::class.java)

        return if(result.isEmpty()) Option.none()
               else Option.of(result.first())
    }

}

