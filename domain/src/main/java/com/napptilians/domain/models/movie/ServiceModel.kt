package com.napptilians.domain.models.movie

import java.time.ZonedDateTime

data class ServiceModel(
    val serviceId: Long,
    val categoryId: Long,
    val name: String,
    val description: String,
    val image: ByteArray,
    val day: String,
    val date: ZonedDateTime?,
    val spots: Int,
    val durationMin: Int,
    val ownerId: String
)
