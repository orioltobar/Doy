package com.napptilians.domain.models.movie

import java.time.ZonedDateTime

data class ServiceModel(
    var serviceId: Long? = null,
    var categoryId: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var image: ByteArray? = null,
    var day: String? = null,
    val date: ZonedDateTime? = null,
    var spots: Int? = null,
    var durationMin: Int? = null,
    var ownerId: String? = null
)
