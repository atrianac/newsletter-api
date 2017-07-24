package com.springernature.newsletter.service

import com.springernature.newsletter.model.Book
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.model.Subscriber
import com.springernature.newsletter.repository.INotificationCustomRepository

import com.springernature.newsletter.repository.NotificationRepository
import com.springernature.newsletter.repository.SubscriberRepository
import javaslang.Tuple
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.springframework.stereotype.Service

interface INewslettersService {

    fun createNotificationForBook(book: Book): Deferred<Notification>

    fun findNewslettersByEmail(email: String): Newsletter
}

@Service
open class NewslettersService(val notificationRepository: NotificationRepository, val categoryService: ICategoriesService,
                         val subscriberRepository: SubscriberRepository, val notificationCustomRepository: INotificationCustomRepository) : INewslettersService {

    override fun createNotificationForBook(book: Book) = async(CommonPool)  {
            val categoryPath = book.categoryCodes.map { categoryService.calculateCategoryPath(it)}
            val notification = Notification(book.title, categoryPath, categoryPath.flatMap { it })

        notificationRepository.save(notification)
    }

    override fun findNewslettersByEmail(email: String): Newsletter {
        val subscriber = subscriberRepository.findOne(email)
        val subscriberCategories = subscriber.categoryCodes

        val notifications = notificationCustomRepository.findByCategories(subscriberCategories)
        return Newsletter(subscriber.email, filterNotifications(notifications, subscriber))
    }

    private fun filterNotifications(notifications: List<Notification>, subscriber: Subscriber): List<Notification>  {
        val categoriesMap = javaslang.collection.List.ofAll(subscriber.categoryCodes).toMap { Tuple.of(it, it) }

        fun filterCategoryList(categories: List<String>): List<String> {
            fun iter(validCategories: List<String>) : List<String> =
                if(validCategories.isEmpty() || categoriesMap[validCategories.first()].isDefined) validCategories
                else iter(validCategories.drop(1))
            return iter(categories)
        }


        return notifications.map { Notification(it.title, it.categoryPath.map { filterCategoryList(it) }, it.categories ) }
    }

}