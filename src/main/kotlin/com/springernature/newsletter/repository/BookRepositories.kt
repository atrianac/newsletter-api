package com.springernature.newsletter.repository

import com.springernature.newsletter.model.Book
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface BookRepository : MongoRepository<Book, String>

interface IBookCustomRepository {
    fun findBooksByCategories(categories: List<String>): List<Book>
}

@Repository
open class BookCustomRepository(val mongoTemplate: MongoTemplate) : IBookCustomRepository {

    override fun findBooksByCategories(categories: List<String>): List<Book> {
        val query = Query().addCriteria(Criteria.where("categoryCodes").`in`(categories))
        return mongoTemplate.find(query, Book::class.java)
    }
}
