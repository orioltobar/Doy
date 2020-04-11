package com.napptilians.domain.models.movie

data class CategoryModel(
    val categoryId: Long,
    val pictureUrl: String,
    val name: String,
    var isSelected: Boolean = false
)
