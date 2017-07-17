package com.springernature.newsletter.model

data class Newsletter(val recipient: String, val notifications: List<Notification>)

data class Notification(val book: String, val categoryPaths: List<List<String>>)
