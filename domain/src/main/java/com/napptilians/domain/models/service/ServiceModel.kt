package com.napptilians.domain.models.service

import java.io.Serializable
import java.time.ZonedDateTime

data class ServiceModel(
    var serviceId: Long? = null,
    var categoryId: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var day: String? = null,
    var hour: String? = null,
    val date: ZonedDateTime? = null,
    var spots: Int? = null,
    var durationMin: Int? = null,
    var ownerId: String? = null,
    val assistance: Boolean = false
): Serializable
