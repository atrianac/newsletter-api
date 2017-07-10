package com.springernature.newsletter.model

import javaslang.collection.List

data class Book(val title: String, val categories: List<Category>)