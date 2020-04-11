package com.napptilians.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class UserUpdateRequestApiModel(
    @SerializedName("uid")
    val uid: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("token")
    val pushToken: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("image")
    val image: String? = null
)