package com.napptilians.domain.models.movie

data class ServiceModel(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val image: ByteArray,
    val day: String,
    val spots: Int,
    val durationMin: Int,
    val ownerId: String
)
