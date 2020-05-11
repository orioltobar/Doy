package com.napptilians.domain.models.category

data class CategoryModel(
    val categoryId: Long,
    val pictureUrl: String,
    val name: String,
    var isSelected: Boolean = false,
    var shouldBeSelected: Boolean = true
)
