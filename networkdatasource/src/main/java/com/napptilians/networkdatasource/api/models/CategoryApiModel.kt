package com.napptilians.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class CategoryApiModel(
    @SerializedName("category_id")
    val categoryId: Long? = null,

    @SerializedName("picture_url")
    val pictureUrl: String? = null,

    @SerializedName("name")
    val name: String? = null
)
