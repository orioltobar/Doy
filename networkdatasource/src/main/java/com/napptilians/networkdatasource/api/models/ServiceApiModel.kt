package com.napptilians.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class ServiceApiModel(
    @SerializedName("service_id")
    val serviceId: Long? = null,
    @SerializedName("category_id")
    val categoryId: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("hour")
    val hour: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("spots")
    val spots: Int? = null,
    @SerializedName("attendees")
    val attendees: Int? = null,
    @SerializedName("duration")
    val durationMin: Int? = null,
    @SerializedName("owner_id")
    val ownerId: String? = null,
    @SerializedName("owner_image")
    val ownerImage: String? = null,
    @SerializedName("assistance")
    val assistance: String? = null
)
