package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.repository.INotificationCustomRepository

import com.springernature.newsletter.repository.NotificationRepository
import com.springernature.newsletter.repository.SubscriberRepository
import javaslang.collection.List
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.springframework.stereotype.Service

interface INewslettersService {

    fun createNotificationForBook(book: Book): Notification

    fun findNewslettersByEmail(email: String): Newsletter
}

@Service
class NewslettersService(val notificationRepository: NotificationRepository, val categoryService: ICategoriesService,
                         val subscriberRepository: SubscriberRepository, val notificationCustomRepository: INotificationCustomRepository) : INewslettersService {

    override fun createNotificationForBook(book: Book): Notification  {
            val categoryPath = List.ofAll(book.categoryCodes).map { categoryService.calculateCategoryPath(it)}.toJavaList()
            val notification = Notification(book.title, categoryPath, categoryPath.flatMap { it })

            return notificationRepository.save(notification)
    }

    override fun findNewslettersByEmail(email: String): Newsletter {
        val subscriber = subscriberRepository.findOne(email)
        val subscriberCategories = subscriber.categoryCodes

        val notifications = notificationCustomRepository.findByCategories(subscriberCategories)
        return Newsletter(subscriber.email, notifications)
    }

}