package com.springernature.newsletter.repository

import com.springernature.newsletter.model.Book
import org.springframework.data.mongodb.repository.MongoRepository

interface BookRepository : MongoRepository<Book, String>
