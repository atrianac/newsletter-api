package com.springernature.newsletter.repository

import com.springernature.newsletter.model.Category
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface CategoryRepository : MongoRepository<Category, String>

interface ICategoryCustomRepository {
    fun findByListOfCategories(categories: List<String>) : List<Category>
}

@Repository
open class CategoryCustomRepository(val mongoTemplate: MongoTemplate) : ICategoryCustomRepository {
    override fun findByListOfCategories(categories: List<String>) : List<Category> {
        val query = Query().addCriteria(Criteria.where("code").`in`(categories))
        return mongoTemplate.find(query, Category::class.java)
    }
}
