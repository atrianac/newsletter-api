package com.springernature.newsletter.repository

import com.springernature.newsletter.model.Category
import org.springframework.data.mongodb.repository.MongoRepository

interface CategoryRepository : MongoRepository<Category, String>
