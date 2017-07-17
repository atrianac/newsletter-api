package com.springernature.newsletter.service

import com.springernature.newsletter.model.Category
import com.springernature.newsletter.model.Newsletter
import com.springernature.newsletter.model.Notification
import com.springernature.newsletter.repository.CategoryRepository
import com.springernature.newsletter.repository.IBookCustomRepository
import com.springernature.newsletter.repository.ISubscriberCustomRepository
import javaslang.Tuple
import javaslang.collection.List
import javaslang.collection.Map
import javaslang.collection.SortedSet
import javaslang.control.Option
import org.springframework.stereotype.Service


data class CategoryNode(val category: Category, val childs: SortedSet<CategoryNode>) : Comparable<CategoryNode> {
    override fun compareTo(other: CategoryNode) = category.code.compareTo(other.category.code)

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}

typealias CategoryTree = SortedSet<CategoryNode>

interface INewsletterService {
    fun findNewslettersByEmail(email: String): Newsletter
}

@Service
open class NewsletterService(val bookRepository: IBookCustomRepository, val subscriberRepository: ISubscriberCustomRepository, val categoryRepository: CategoryRepository) : INewsletterService {

    fun buildCategoryTree(categories: List<Category>) {
        fun iter(categories: List<Category>, tree: CategoryTree): CategoryTree =
                if(categories.isEmpty) tree
                else iter(categories.tail(), tree)


    }

    override fun findNewslettersByEmail(email: String): Newsletter {

        println("findNewslettersByEmail: " + email)

        val subscriber = subscriberRepository.findByEmail(email)
        val obooks = subscriber.map { s ->  bookRepository.findBooksByCategories(s.categoryCodes) }

        fun createCategoryMap(): Map<String, String?> {
            val categories = categoryRepository.findAll()
            return  List.ofAll(categories).toMap { item -> Tuple.of(item.code, item.superCategoryCode) }
        }

        val categoriesMap = createCategoryMap()



        fun buildCategoryPath(currentCategory: Option<String?>, path: List<String>): List<String> =
            if(!currentCategory.isDefined) path
            else buildCategoryPath(categoriesMap[currentCategory.get()], path.prepend(currentCategory.get()))

        val notifications = obooks.map { books -> books.map { book -> Notification (book.title, book.categoryCodes.map { categoryCode -> buildCategoryPath(Option.of(categoryCode), List.empty()).toJavaList() }) }}

        return Newsletter(if(subscriber.isDefined) subscriber.get().email else "", notifications.getOrElse(listOf()))

    }



}
