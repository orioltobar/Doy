package com.napptilians.domain.models.service

import org.threeten.bp.ZonedDateTime
import java.io.Serializable

data class ServiceModel(
    var serviceId: Long? = null,
    var categoryId: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var image: String? = null,
    var day: String? = null,
    var hour: String? = null,
    var date: ZonedDateTime? = null,
    var spots: Int? = null,
    var attendees: Int? = null,
    var durationMin: Int? = null,
    var ownerId: String? = null,
    var ownerImage: String? = null,
    val assistance: Boolean = false,
    var isFull: Boolean = false
): Serializable
