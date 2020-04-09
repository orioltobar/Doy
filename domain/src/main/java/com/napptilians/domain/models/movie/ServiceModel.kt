package com.napptilians.domain.models.movie

data class ServiceModel(
    val serviceId: Long? = null,
    val categoryId: Long? = null,
    val name: String? = null,
    val pictureUrl: String? = null,
    val spots: Int? = null,
    val ownerId: Long? = null
)
