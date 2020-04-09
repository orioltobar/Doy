package com.napptilians.domain.models.movie

data class ServiceModel(
    val serviceId: Long,
    val categoryId: Long,
    val name: String,
    val pictureUrl: String,
    val day: String?,
    val spots: Int?,
    val duration: Int?,
    val ownerId: Long?
)
