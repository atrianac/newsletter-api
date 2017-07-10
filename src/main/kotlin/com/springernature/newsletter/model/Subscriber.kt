package com.springernature.newsletter.model

import javaslang.collection.List

data class Subscriber(val email: String, val categories: List<Category>)
